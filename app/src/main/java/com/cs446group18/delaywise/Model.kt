package com.cs446group18.delaywise

import com.cs446group18.lib.models.FlightInfo
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

val client = HttpClient(CIO) {
    install(Logging)
}

object Model {
    suspend fun getFlight() {
        val response = client.get("http://10.0.2.2:8082/flightInfo/AC8838")
        val bodyText = response.bodyAsText()
        val parsedJson = Json { ignoreUnknownKeys = true }.decodeFromString<FlightInfo>(bodyText)
        println(parsedJson)
    }
}
