package com.cs446group18.server.routes

import com.cs446group18.server.ServerModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.airportInfo() {
    get("/airports/{airport_code}/flights/departures") {
        var airportCode = call.parameters["airport_code"]
        airportCode ?: throw Exception("airport_code not provided")
        val response = ServerModel.getAirportDelayRaw(airportCode)
        val strResponse = Json.encodeToString(response)
        call.respondText(strResponse, ContentType.Application.Json)
    }
}
