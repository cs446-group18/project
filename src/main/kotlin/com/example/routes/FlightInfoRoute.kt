package com.example.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.flightInfo() {
    get("/") {
        val client = HttpClient()
        val response = client.get("https://airlabs.co/api/v9/routes?api_key=b0134401-3dd2-469c-85cf-4974afbf338b&flight_iata=AC8838")
        val body :String= response.body()
        call.respondText(body)
    }
}