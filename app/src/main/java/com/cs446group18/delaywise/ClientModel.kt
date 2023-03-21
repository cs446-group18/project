package com.cs446group18.delaywise

import com.cs446group18.lib.models.Fetcher
import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.Model
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val client = HttpClient(CIO) {
    install(Logging)
}

data class ClientFetcher(
    val apiKey: String? = null
) : Fetcher {
    override suspend fun makeAeroApiCall(url: String): HttpResponse {
        val baseUrl = when(apiKey) {
            null -> "http://10.0.2.2:8082"
            else -> "https://aeroapi.flightaware.com/aeroapi"
        }
        val response = client.get(baseUrl + url) {
            headers {
                if(apiKey != null) {
                    append("x-apikey", apiKey)
                }
            }
        }
        if(response.status != HttpStatusCode.OK) {
            throw Exception("returned invalid status code ${response.status}")
        }
        return response
    }
}

suspend fun Model.getFlight(flightIata: String) : FlightInfo {
    val flightInfoResponse = getFlightRaw(flightIata)
    val activeInstance = flightInfoResponse.flights.find {
        it.scheduled_out.toLocalDateTime(TimeZone.currentSystemDefault()).date ==
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    } ?: flightInfoResponse.flights.minBy {
        (it.scheduled_out - Clock.System.now()).absoluteValue
    }
    return activeInstance
}

val ClientModel = Model(ClientFetcher())
