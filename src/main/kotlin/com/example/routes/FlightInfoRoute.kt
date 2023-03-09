package com.example.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val API_KEY_AIRLABS = "b0134401-3dd2-469c-85cf-4974afbf338b"
fun Route.flightInfo() {
    get("/flightInfo/{id}") {
        val flightIata = call.parameters["id"]
        println("flight iata is $flightIata")
        val client = HttpClient()
        val response = client.get("https://airlabs.co/api/v9/routes?api_key=$API_KEY_AIRLABS&flight_iata=$flightIata")
        val body :String= response.body()
        call.respondText(body)
    }
    get("/") {
        call.respond(200)
    }
}