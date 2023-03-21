package com.cs446group18.server

import com.cs446group18.lib.models.Fetcher
import com.cs446group18.lib.models.Model
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

const val API_KEY_AERO = "Yyj2JugSpnL9ZUITA7QSTg4fkstUqsUK"

val client = HttpClient()

class ServerFetcher : Fetcher {
    override suspend fun makeAeroApiCall(url: String): HttpResponse {
        val response = client.get("https://aeroapi.flightaware.com/aeroapi$url") {
            headers {
                append("x-apikey", API_KEY_AERO)
            }
        }
        if(response.status != HttpStatusCode.OK) {
            throw Exception("returned invalid status code ${response.status}")
        }
        return response
    }
}

val ServerModel = Model(ServerFetcher())
