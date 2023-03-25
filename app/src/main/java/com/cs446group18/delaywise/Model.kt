package com.cs446group18.delaywise

import com.cs446group18.lib.models.FlightInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json.Default.decodeFromJsonElement
import kotlinx.serialization.json.Json.Default.decodeFromString

val client = HttpClient(CIO) {
    install(Logging)
}

object Model {
    suspend fun getFlight(flightIata: String) : FlightInfo {
        val response = client.get("http://10.0.2.2:8082/flightInfo/${flightIata}")
        val bodyText = response.bodyAsText()
        val parsedJson = Json { ignoreUnknownKeys = true }.decodeFromString<FlightInfo>(bodyText)
        return parsedJson
    }

    suspend fun getAirportDelay(airport: String): List<Int> {
        val response = client.get("http://10.0.2.2:8082/airportDelay/${airport}")
        val delays = mutableListOf<Int>()
        val textResponse = response.bodyAsText()
        val parsedJson = Json { ignoreUnknownKeys = true }.decodeFromString<IntArray>(textResponse)
        for (item in parsedJson) {
            delays.add(item)
        }
        return delays
    }
}
