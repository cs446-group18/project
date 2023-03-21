package com.cs446group18.server.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.time.LocalDate

//Wen's api key
const val API_KEY_AERO_2 = "lY2EJ3AcgGkFcpjJ5CgKWYrZDGy211A5"

fun Route.airportInfo() {
    get("/airport/{airport_code}") {
        println("hit /airport/{airport_code}")
        var airportCode = call.parameters["airport_code"]
        println("airportCode is $airportCode")
        var client = HttpClient()

        val end = LocalDate.now()
        val start = LocalDate.now().minusDays(1)


        var httpResponse = client.get("$BASE_URL_AERO/airports/$airportCode/flights"){
            headers {
                append("x-apikey", API_KEY_AERO_2)
                parameter("start", start.toString())
                parameter("end", end.toString())
            }
        }
        var responseObject: JsonObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())

        client.close()
        call.respond(responseObject)
    }
}