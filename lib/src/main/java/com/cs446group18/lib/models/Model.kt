package com.cs446group18.lib.models

import com.google.common.util.concurrent.RateLimiter
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.time.Duration
import kotlin.collections.set
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val json = Json { ignoreUnknownKeys = true }
class NoFlightsFoundException(message: String = "No flights found") : Exception(message)

interface Fetcher {
    suspend fun makeAeroApiCall(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse
}

interface Cache<T> {
    suspend fun insert(id: String, item: T)
    suspend fun lookup(id: String): T?
}

/**
 * Class to be used as a singleton for retrieving and storing data
 * from the external API. Useful because we have very similar query
 * patterns on the Android client and the backend server (caching layer).
 */
open class Model(
    private val fetcher: Fetcher,
    private val flightInfoCache: Cache<FlightInfoResponse>,
    private val airportDelayCache: Cache<AirportDelayResponse>,
    private val scheduledFlightCache: Cache<ScheduledFlightsResponse>,
    private val airportCache: Cache<Airport>,
    private val weatherCache: Cache<WeatherResponse>,
) {
    val rateLimiter = RateLimiter.create(10.0/60.0)

    suspend fun getFlightRaw(flightCode: String) : FlightInfoResponse {
        val cached = flightInfoCache.lookup(flightCode)
        if(cached != null) return cached
        rateLimiter.acquire(1)
        val response = fetcher.makeAeroApiCall("/flights/${flightCode}")
        println(response.bodyAsText())
        val decoded = json.decodeFromString<FlightInfoResponse>(response.bodyAsText())
        flightInfoCache.insert(flightCode, decoded)
        if(decoded.flights.isEmpty()) {
            throw NoFlightsFoundException("No flights found with code $flightCode")
        }
        return decoded
    }
    suspend fun getScheduledFlights(flightCode: String) : ScheduledFlightsResponse {
        val cached = scheduledFlightCache.lookup(flightCode)
        if(cached != null) return cached
        val (airlineIata, flightNumber) = destructureFlightCode(flightCode)

        val startInterval = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val endInterval = startInterval + DatePeriod(days=15)
        rateLimiter.acquire(3)
        val response = fetcher.makeAeroApiCall("/schedules/$startInterval/$endInterval") {
            parameter("airline", airlineIata)
            parameter("flight_number", flightNumber)
            parameter("max_pages", 3)
        }
        val decoded = json.decodeFromString<ScheduledFlightsResponse>(response.bodyAsText())
        scheduledFlightCache.insert(flightCode, decoded)
        if(decoded.scheduled.isEmpty()) {
            throw NoFlightsFoundException("No flights found with code $flightCode")
        }
        return decoded
    }

    suspend fun getWeatherRaw(airportCode: String): WeatherResponse {
        val cached = weatherCache.lookup(airportCode)
        if(cached != null) return cached
        val currentDateTime = truncateToHours(Clock.System.now())
        rateLimiter.acquire(1)
        val response = fetcher.makeAeroApiCall("/airports/$airportCode/weather/observations") {
            parameter("timestamp", currentDateTime)
        }
        val decoded = json.decodeFromString<WeatherResponse>(response.bodyAsText())
        weatherCache.insert(airportCode, decoded)
        return decoded
    }
    suspend fun getAirport(airportCode: String) : Airport {
        val cached = airportCache.lookup(airportCode)
        if(cached != null) return cached

        rateLimiter.acquire(1)
        val response = fetcher.makeAeroApiCall("/airports/$airportCode")
        val decoded = json.decodeFromString<Airport>(response.bodyAsText())
        airportCache.insert(airportCode, decoded)
        return decoded
    }
    suspend fun getAirportDelayRaw(airportCode: String,
                                   intervalEnd: Instant = truncateToHours(Clock.System.now()),
                                   intervalStart: Instant = intervalEnd - HOURS_IN_AIRPORT_DELAY_GRAPH.toDuration(DurationUnit.HOURS)) : AirportDelayResponse {
        val cached = airportDelayCache.lookup(airportCode)
        if(cached != null) return cached
        rateLimiter.acquire(3)
        val response = fetcher.makeAeroApiCall("/airports/$airportCode/flights/departures") {
            parameter("start", intervalStart.toLocalDateTime(TimeZone.UTC))
            parameter("end", intervalEnd.toLocalDateTime(TimeZone.UTC))
            parameter("max_pages", 3)
        }
        val filteredBody = filterBadFlights(response.bodyAsText())
        val decoded = json.decodeFromString<AirportDelayResponse>(filteredBody)
        airportDelayCache.insert(airportCode, decoded)
        if(decoded.departures.isEmpty()) {
            throw NoFlightsFoundException("No flights found from airport $airportCode")
        }
        return decoded
    }
}

fun truncateToHours(instant: Instant) : Instant {
    val time = instant.toLocalDateTime(TimeZone.UTC)
    val nanoSeconds = time.nanosecond.toDuration(DurationUnit.NANOSECONDS)
    val seconds = time.second.toDuration(DurationUnit.SECONDS)
    val minutes = time.minute.toDuration(DurationUnit.MINUTES)
    return instant - minutes - seconds - nanoSeconds
}

fun filterBadFlights(bodyText: String, flightArrayKey: String = "departures"): String {
    val responseObj = json.decodeFromString(JsonObject.serializer(), bodyText)
    val departures = responseObj[flightArrayKey]?.jsonArray ?: JsonArray(listOf())
    val filteredDepartures = departures.filter {
        doesJsonContainStrings(it, listOf(
            "ident_iata",
            "scheduled_out",
            "scheduled_in",
        ))
    }
    val newResponseObj = responseObj.toMutableMap()
    newResponseObj[flightArrayKey] = JsonArray(filteredDepartures)
    return Json.encodeToString(JsonObject(newResponseObj))
}
fun doesJsonContainStrings(root: JsonElement, keys: List<String>): Boolean {
    for(key in keys) {
        val fieldIsString = root.jsonObject[key]?.jsonPrimitive?.isString
        if (fieldIsString == null || !fieldIsString) return false
    }
    return true
}

fun destructureFlightCode(flightCode: String): MatchResult.Destructured {
    val match = """^(.*?)(\d+)$""".toRegex().matchEntire(flightCode)
    match ?: throw Exception("could not extract airline code from $flightCode")
    return match.destructured
}
