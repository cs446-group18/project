package com.cs446group18.server.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

//Wen's api key
const val API_KEY_AERO_2 = "lY2EJ3AcgGkFcpjJ5CgKWYrZDGy211A5"

fun Route.airportInfo() {
    get("/airport/{airport_code}") {
        println("hit /airport/{airport_code}")
        var airportCode = call.parameters["airport_code"]
        println("airportCode is $airportCode")
        var client = HttpClient()

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS) // current time rounded down to the closest hour
        val eightHoursAgo = now.minusHours(8) // time 8 hours ago from current time
        println("startTime is $eightHoursAgo")
        println("endTime is $now")

        var httpResponse = client.get("$BASE_URL_AERO/airports/$airportCode/flights"){
            headers {
                append("x-apikey", API_KEY_AERO_2)
                parameter("start", eightHoursAgo)
                parameter("end", now)
            }
        }
        var responseObject: JsonObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())
        println(responseObject.toString())
        client.close()
        call.respond(responseObject)
    }
}