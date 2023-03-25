package com.cs446group18.lib.models

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.*
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.DurationUnit
import kotlin.time.toDuration

interface Fetcher {
    suspend fun makeAeroApiCall(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
}

sealed interface Cacheable
interface Cache<T> {
    suspend fun insert(id: String, item: T)
    suspend fun lookup(id: String): T?
}

fun truncateToHours(instant: Instant) : Instant {
    val time = instant.toLocalDateTime(TimeZone.UTC)
    val nanoSeconds = time.nanosecond.toDuration(DurationUnit.NANOSECONDS)
    val seconds = time.second.toDuration(DurationUnit.SECONDS)
    val minutes = time.minute.toDuration(DurationUnit.MINUTES)
    return instant - minutes - seconds - nanoSeconds
}

open class Model(
    private val fetcher: Fetcher,
    private val flightInfoCache: Cache<FlightInfoResponse>,
    private val airportDelayCache: Cache<AirportDelayWrapper>,
) {
    suspend fun getFlightRaw(flightCode: String) : FlightInfoResponse {
        val cached = flightInfoCache.lookup(flightCode)
        if(cached != null) return cached

        val response = fetcher.makeAeroApiCall("/flights/${flightCode}")
        val decoded = Json { ignoreUnknownKeys = true }.decodeFromString<FlightInfoResponse>(response.bodyAsText())
        flightInfoCache.insert(flightCode, decoded)
        if(decoded.flights.isEmpty()) {
            throw Exception("no flights found with code $flightCode")
        }
        return decoded
    }

    suspend fun getAirportDelay(airportCode: String) : AirportDelayWrapper {
        val cached = airportDelayCache.lookup(airportCode)
        if(cached != null) return cached

        val intervalEnd = truncateToHours(Clock.System.now())
        val intervalStart = intervalEnd - HOURS_IN_AIRPORT_DELAY_GRAPH.toDuration(DurationUnit.HOURS)

        val response = fetcher.makeAeroApiCall("/airports/$airportCode/flights/departures") {
            parameter("start", intervalStart.toLocalDateTime(TimeZone.UTC))
            parameter("end", intervalEnd.toLocalDateTime(TimeZone.UTC))
            parameter("max_pages", 5)
        }
        var wrapper: AirportDelayWrapper
        try {
            val decoded = Json {
                ignoreUnknownKeys = true
            }.decodeFromString<AirportDelayResponse>(response.bodyAsText())
            wrapper = AirportDelayWrapper(
                response = decoded,
                intervalStart = intervalStart,
                intervalEnd = intervalEnd,
            )
        } catch(exception: MissingFieldException) {
            wrapper = Json {
                ignoreUnknownKeys = true
            }.decodeFromString(response.bodyAsText())
        }
        airportDelayCache.insert(airportCode, wrapper)
        if(wrapper.response.departures.isEmpty()) {
            throw Exception("no flights found from airport $airportCode")
        }
        return wrapper
    }
}
