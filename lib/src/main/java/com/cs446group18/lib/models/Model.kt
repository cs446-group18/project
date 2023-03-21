package com.cs446group18.lib.models

import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface Fetcher {
    suspend fun makeAeroApiCall(url: String): HttpResponse
}

abstract class Model(
    private val fetcher: Fetcher
) {
    suspend fun getFlightRaw(flightICAO: String) : FlightInfoResponse {
        val response = fetcher.makeAeroApiCall("/flights/${flightICAO}")
        // TODO: check errors and cache response
        val decoded = Json { ignoreUnknownKeys = true }.decodeFromString<FlightInfoResponse>(response.bodyAsText())
        if(decoded.flights.isEmpty()) {
            throw Exception("no flights found with code $flightICAO")
        }
        return decoded
    }
}
