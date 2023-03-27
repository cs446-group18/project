package com.cs446group18.delaywise.model

import android.content.Context
import androidx.room.*
import com.cs446group18.lib.models.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.DurationUnit
import kotlin.time.toDuration


val client = HttpClient(CIO) {
    install(Logging)
}

data class ClientFetcher(
    val apiKey: String? = null,
) : Fetcher {
    override suspend fun makeAeroApiCall(
        url: String,
        block: HttpRequestBuilder.() -> Unit
    ): HttpResponse {
        val baseUrl = when (apiKey) {
            null -> "http://10.0.2.2:8082"
            else -> "https://aeroapi.flightaware.com/aeroapi"
        }
        val response = client.get(baseUrl + url) {
            headers {
                if (apiKey != null) {
                    append("x-apikey", apiKey)
                }
            }
            block(this)
        }
        if (response.status != HttpStatusCode.OK) {
            throw Exception("returned invalid status code ${response.status}")
        }
        return response
    }
}

@Database(
    entities = [
        FlightInfoEntity::class,
        AirportDelayEntity::class,
    ], version = 3, exportSchema = false
)
abstract class DelayWiseLocalDatabase : RoomDatabase() {
    abstract fun flightInfoDao(): FlightInfoDao
    abstract fun airportDelayDao(): AirportDelayDao
}

class ClientModel(
    fetcher: ClientFetcher,
    flightInfoCache: ClientCache<FlightInfoEntity, FlightInfoResponse>,
    airportDelayCache: ClientCache<AirportDelayEntity, AirportDelayResponse>,
    val airlinesByIata: Map<String, Airline>,
    val airportsByIata: Map<String, Airport>,
) : Model(
    fetcher = fetcher,
    flightInfoCache = flightInfoCache,
    airportDelayCache = airportDelayCache,
) {

    companion object {
        @Volatile
        private var INSTANCE: ClientModel? = null
        fun getInstance(): ClientModel {
            return INSTANCE
                ?: throw Exception("ClientModel not yet initialized; only call ClientModel.getInstance() after setup in MainActivity.kt")
        }

        fun init(context: Context) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                DelayWiseLocalDatabase::class.java,
                "delaywise_local_database"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = ClientModel(
                fetcher = ClientFetcher(),
                flightInfoCache = ClientCache(
                    dao = db.flightInfoDao(),
                    createEntity = ::FlightInfoEntity,
                    encode = { Json.encodeToString(it) },
                    decode = { Json.decodeFromString(it) },
                    maxCacheTime = 3.toDuration(DurationUnit.MINUTES)
                ),
                airportDelayCache = ClientCache(
                    dao = db.airportDelayDao(),
                    createEntity = ::AirportDelayEntity,
                    encode = { Json.encodeToString(it) },
                    decode = { Json.decodeFromString(it) },
                    maxCacheTime = 30.toDuration(DurationUnit.MINUTES)
                ),
                airlinesByIata = loadMapping(context, "airline_codes.csv"),
                airportsByIata = loadMapping(context, "airport_codes.csv"),
            )
        }
    }

    suspend fun getFlight(flightIata: String): FlightInfo {
        val match = """^(.*?)(\d+)$""".toRegex().matchEntire(flightIata)
        match ?: throw Exception("could not extract airline code from $flightIata")
        val (airlineIata, flightNumber) = match.destructured
        val airlineIcao = airlinesByIata[airlineIata]?.icao
            ?: throw Exception("could not find matching an airline matching $airlineIata")
        val flightInfoResponse = getFlightRaw(airlineIcao + flightNumber)
        val activeInstance = flightInfoResponse.flights.find {
            it.scheduled_out.toLocalDateTime(TimeZone.currentSystemDefault()).date ==
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        } ?: flightInfoResponse.flights.minBy {
            (it.scheduled_out - Clock.System.now()).absoluteValue
        }
        return activeInstance
    }

    suspend fun getAirportDelay(airportCode: String): AirportDelayWrapper {
        val intervalEnd = truncateToHours(Clock.System.now())
        val intervalStart = intervalEnd - HOURS_IN_AIRPORT_DELAY_GRAPH.toDuration(DurationUnit.HOURS)
        return AirportDelayWrapper(
            response = getAirportDelayRaw(airportCode),
            intervalStart = intervalStart,
            intervalEnd = intervalEnd,
        )
    }
}

