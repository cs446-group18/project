package com.example.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

val API_KEY_AIRLABS = "b0134401-3dd2-469c-85cf-4974afbf338b"
fun Route.flightInfo() {
    get("/flightInfo/{id}") {
        val flightIata = call.parameters["id"]
        val client = HttpClient()
        val response = client.get("https://airlabs.co/api/v9/routes?api_key=$API_KEY_AIRLABS&flight_iata=$flightIata")
        val body: String= response.body()
        val json = Json { ignoreUnknownKeys = true }
        val jsonObject: JsonObject = json.decodeFromString(JsonObject.serializer(), body)

        // Accessing values in the JsonObject
        val responseArray: JsonArray = jsonObject["response"] as JsonArray
        val responseJsonObject: JsonObject = responseArray.first() as JsonObject
        val depTime: JsonElement? = responseJsonObject["dep_time"]
        println(depTime)

        call.respondText(depTime.toString())
    }
    get("/") {
        call.respond(200)
    }
}