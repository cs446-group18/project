package com.cs446group18.delaywise.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cs446group18.delaywise.util.formatAsShortDate
import com.cs446group18.lib.models.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Integer.max
import kotlin.time.DurationUnit
import kotlin.time.Duration
import kotlin.time.toDuration
import kotlin.time.ExperimentalTime
import kotlin.time.days


val client = HttpClient(CIO) {
    install(Logging)
    install(HttpTimeout) {
        requestTimeoutMillis = 7000;
        connectTimeoutMillis = 7000;
        socketTimeoutMillis = 7000;
    }
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
        SavedFlightEntity::class,
        WeatherInfoEntity::class,
        AirportInfoEntity::class,
        SavedAirportEntity::class,
    ], version = 6, exportSchema = false
)
abstract class DelayWiseLocalDatabase : RoomDatabase() {
    abstract fun flightInfoDao(): FlightInfoDao
    abstract fun airportDelayDao(): AirportDelayDao
    abstract fun scheduledFlightDao(): ScheduledFlightDao
    abstract fun weatherDao(): WeatherInfoDao
    abstract fun airportDao(): AirportInfoDao
    abstract fun savedFlightDao(): SavedFlightDao
    abstract fun savedAirportDao(): SavedAirportDao
}

class ClientModel(
    val model : Model,
    val airlinesByIata: Map<String, Airline>,
    val airportsByIata: Map<String, Airport>,
    val savedFlightDao: SavedFlightDao,
    val savedAirportDao: SavedAirportDao
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

    suspend fun getFlight(flightIata: String, date: LocalDate? = null): Pair<FlightInfo, List<FlightInfo>> {
        val match = """^(.*?)(\d+)$""".toRegex().matchEntire(flightIata)
        match ?: throw Exception("could not extract airline code from $flightIata")
        val (airlineIata, flightNumber) = match.destructured
        val airlineIcao = airlinesByIata[airlineIata]?.icao
            ?: throw Exception("could not find an airline matching $airlineIata")
        val flightInfoResponse = model.getFlightRaw(airlineIcao + flightNumber)
        val existingOutTimes = flightInfoResponse.flights.map{ it.scheduled_out }.toHashSet()
        val scheduledFlights = model.getScheduledFlights(airlineIcao + flightNumber).scheduled.filter {
            (it.actual_ident_iata == null || it.ident_iata == it.actual_ident_iata) && it.scheduled_out !in existingOutTimes
        }
        val templateFlight = flightInfoResponse.flights.first()
        val flightsIncludingScheduled = (flightInfoResponse.flights + scheduledFlights.map{ it.toFlightInfo(templateFlight) }).sortedBy { it.scheduled_out }
        val selectedFlight = pickFlight(date, flightsIncludingScheduled) ?: flightsIncludingScheduled.minBy{ it.scheduled_out }
        return Pair(selectedFlight, flightsIncludingScheduled)
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
                    createEntity = ::AirportInfoEntity,
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
            savedFlightDao = db.savedFlightDao(),
            savedAirportDao = db.savedAirportDao()
        )
    }
}

fun List<FlightInfo>.filterPickableFlights(): List<FlightInfo> {
    return filter {
        it.scheduled_out >= Clock.System.now() - 6.toDuration(DurationUnit.HOURS)
    }
}

fun pickFlight(date: LocalDate?, flightArray: List<FlightInfo>): FlightInfo? {
    return when(date) {
        null -> flightArray.filterPickableFlights().minByOrNull { it.scheduled_out }
        else -> flightArray.find { it.getDepartureDate() == date }
    }
}

fun List<FlightInfo>.calcHistorical(): HistoricalInfo {
    var totalDelay = 0
    var delayedFlights = 0      // 15 minutes and up
    var cancelledFlights = 0
    var numFlights = 0
    var historical: HistoricalInfo = HistoricalInfo()

    for (flightInfo in this.sortedBy { it.scheduled_out }) {
        if (flightInfo.scheduled_in <= Clock.System.now() && flightInfo.scheduled_out >= Clock.System.now() - 10.toDuration(DurationUnit.DAYS)) {
            var delay = flightInfo.departure_delay_raw / 60
            if (flightInfo.status == "Cancelled") {
                cancelledFlights++
                numFlights++
            }
            else if (delay != null) {
                // calculate
                if (delay > 0) {
                    totalDelay += delay
                    delayedFlights++
                }
                numFlights++

                // add to list
                historical.delayDates.add(flightInfo.scheduled_out.formatAsShortDate())
                historical.delayLengths.add(max(delay, 0))
            }
        }
    }
    historical.delayRate = delayedFlights * 100 / numFlights
    historical.cancellationRate = cancelledFlights * 100 / numFlights
    historical.averageDelay = totalDelay / numFlights
    return historical
}

