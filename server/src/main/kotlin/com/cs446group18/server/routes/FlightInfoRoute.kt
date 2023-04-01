package com.cs446group18.server.routes

import com.cs446group18.server.ServerModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.flightInfo() {
    get("/flights/{flight_iata}"){
        val flightIata = call.parameters["flight_iata"]
        flightIata ?: throw Exception("flightIata not provided")
        val response = ServerModel.getFlightRaw(flightIata)
        val strResponse = Json.encodeToString(response)
        call.respondText(strResponse, ContentType.Application.Json)
    }
    get("/schedules/{start_interval}/{end_interval}") {
        val airline = call.parameters["airline"]
        val flightNumber = call.parameters["flight_number"]
        airline ?: throw Exception("airline not provided")
        flightNumber ?: throw Exception("flightNumber not provided")
        val flightIata = airline + flightNumber
        val response = ServerModel.getScheduledFlights(flightIata)
        val strResponse = Json.encodeToString(response)
        call.respondText(strResponse, ContentType.Application.Json)
    }
}
