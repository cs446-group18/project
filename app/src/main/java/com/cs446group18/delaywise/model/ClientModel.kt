package com.cs446group18.delaywise.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cs446group18.lib.models.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
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
        ScheduledFlightEntity::class,
        AirportEntity::class,
        WeatherInfoEntity::class,
    ], version = 5, exportSchema = false
)
abstract class DelayWiseLocalDatabase : RoomDatabase() {
    abstract fun flightInfoDao(): FlightInfoDao
    abstract fun airportDelayDao(): AirportDelayDao
    abstract fun scheduledFlightDao(): ScheduledFlightDao
    abstract fun airportDao(): AirportDao
    abstract fun weatherDao(): WeatherInfoDao
}

class ClientModel(
    val model : Model,
    val airlinesByIata: Map<String, Airline>,
    val airportsByIata: Map<String, Airport>,
) {
    companion object {
        @Volatile
        private var INSTANCE: ClientModel? = null
        fun getInstance(): ClientModel {
            return INSTANCE
                ?: throw Exception("ClientModel not yet initialized; only call ClientModel.getInstance() after setup in MainActivity.kt")
        }
        fun init(context: Context) { INSTANCE = ClientModelFactory.createClientModel(context) }
    }
    suspend fun getAirport(airportCode: String) = model.getAirport(airportCode)

    suspend fun getWeather(airportCode: String) = model.getWeatherRaw(airportCode).observations.first()

    suspend fun getFlight(flightIata: String, date: LocalDate? = null): FlightInfo {
        val match = """^(.*?)(\d+)$""".toRegex().matchEntire(flightIata)
        match ?: throw Exception("could not extract airline code from $flightIata")
        val (airlineIata, flightNumber) = match.destructured
        val airlineIcao = airlinesByIata[airlineIata]?.icao
            ?: throw Exception("could not find matching an airline matching $airlineIata")
        val flightInfoResponse = model.getFlightRaw(airlineIcao + flightNumber)
        var selectedFlight = pickFlight(date, flightInfoResponse.flights)
        if(selectedFlight == null) {
            val scheduledFlightsResponse = model.getScheduledFlights(airlineIcao + flightNumber)
            val templateFlight = flightInfoResponse.flights.first()
            val flightsIncludingScheduled = flightInfoResponse.flights + scheduledFlightsResponse.scheduled.map{ it.toFlightInfo(templateFlight) }
            selectedFlight = pickFlight(date, flightsIncludingScheduled) ?: flightsIncludingScheduled.minBy{ it.scheduled_out }
        }
        return selectedFlight!!
    }

    suspend fun getAirportDelay(airportCode: String): AirportDelayWrapper {
        val intervalEnd = truncateToHours(Clock.System.now())
        val intervalStart = intervalEnd - HOURS_IN_AIRPORT_DELAY_GRAPH.toDuration(DurationUnit.HOURS)
        return AirportDelayWrapper(
            response = model.getAirportDelayRaw(airportCode),
            intervalStart = intervalStart,
            intervalEnd = intervalEnd,
        )
    }
}

object ClientModelFactory {
    fun createClientModel(context: Context): ClientModel {
        val db = Room.databaseBuilder(
            context.applicationContext,
            DelayWiseLocalDatabase::class.java,
            "delaywise_local_database"
        ).fallbackToDestructiveMigration().build()
        return ClientModel(
            model = Model(
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
                scheduledFlightCache = ClientCache(
                    dao = db.scheduledFlightDao(),
                    createEntity = ::ScheduledFlightEntity,
                    encode = { Json.encodeToString(it) },
                    decode = { Json.decodeFromString(it) },
                    maxCacheTime = 6.toDuration(DurationUnit.HOURS)
                ),
                airportCache = ClientCache(
                    dao = db.airportDao(),
                    createEntity = ::AirportEntity,
                    encode = { Json.encodeToString(it) },
                    decode = { Json.decodeFromString(it) },
                    maxCacheTime = 30.toDuration(DurationUnit.DAYS)
                ),
                weatherCache = ClientCache(
                    dao = db.weatherDao(),
                    createEntity = ::WeatherInfoEntity,
                    encode = { Json.encodeToString(it) },
                    decode = { Json.decodeFromString(it) },
                    maxCacheTime = 15.toDuration(DurationUnit.MINUTES)
                )
            ),
            airlinesByIata = loadMapping(context, "airline_codes.csv"),
            airportsByIata = loadMapping(context, "airport_codes.csv"),
        )
    }
}

fun pickFlight(date: LocalDate?, flightArray: List<FlightInfo>) : FlightInfo? {
    return when(date) {
        null -> flightArray.filter {
            it.scheduled_out >= Clock.System.now() - 6.toDuration(DurationUnit.HOURS)
        }.minByOrNull { it.scheduled_out }
        else -> flightArray.find { it.getDepartureDate() == date }
    }
}
