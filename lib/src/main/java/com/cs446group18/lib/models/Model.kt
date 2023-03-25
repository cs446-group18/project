package com.cs446group18.lib.models

import io.ktor.client.statement.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

interface Fetcher {
    suspend fun makeAeroApiCall(url: String): HttpResponse
}

sealed class Cacheable
interface Cache<T> {
    suspend fun insert(id: String, item: T)
    suspend fun lookup(id: String): Pair<T?, Instant?>
}

open class Model(
    private val fetcher: Fetcher,
    val flightInfoCache: Cache<FlightInfoResponse>,
    private val maxCacheTime: Duration = 5.toDuration(DurationUnit.MINUTES),
) {
    suspend fun getFlightRaw(flightICAO: String) : FlightInfoResponse {
        val (cached, cacheTime) = flightInfoCache.lookup(flightICAO)
        if(cached != null && (Clock.System.now() - cacheTime!!) <= maxCacheTime) return cached

        val response = fetcher.makeAeroApiCall("/flights/${flightICAO}")
        // TODO: check errors and cache response
        val decoded = Json { ignoreUnknownKeys = true }.decodeFromString<FlightInfoResponse>(response.bodyAsText())
        flightInfoCache.insert(flightICAO, decoded)
        if(decoded.flights.isEmpty()) {
            throw Exception("no flights found with code $flightICAO")
        }
        return decoded
    }
}
