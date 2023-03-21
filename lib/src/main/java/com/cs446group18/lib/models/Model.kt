package com.cs446group18.lib.models

import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface Fetcher {
    suspend fun makeAeroApiCall(url: String): HttpResponse
}

data class Model(
    private val fetcher: Fetcher
) {
    suspend fun getFlightRaw(flightICAO: String) : FlightInfoResponse {
        val response = fetcher.makeAeroApiCall("/flights/${flightICAO}")
        // TODO: error check and cache response
        return Json { ignoreUnknownKeys = true }.decodeFromString(response.bodyAsText())
    }
}
