package com.example.routes

import com.example.models.FlightInfo
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
import java.time.*
import java.time.format.DateTimeFormatter

const val API_KEY_AIRLABS = "b0134401-3dd2-469c-85cf-4974afbf338b"

fun Route.flightInfo() {
    get("/flightInfo/{flight_iata}") {    // TODO: add date parameter
        // val flightDate = call.parameters["date"]
        val flightIata = call.parameters["flight_iata"]
        val client = HttpClient()
        val json = Json { ignoreUnknownKeys = true }

        // Make API call to AirLabs Route DB for route information
        var httpResponse = client.get("https://airlabs.co/api/v9/routes?api_key=$API_KEY_AIRLABS&flight_iata=$flightIata")
        var responseObject: JsonObject = json.decodeFromString(JsonObject.serializer(), httpResponse.body())
        val routeObject: JsonObject = (responseObject["response"] as JsonArray).first() as JsonObject

        // Retrieve flight info fields from route Json object to create data class
        val flightInfo = FlightInfo(
            flightIata = flightIata!!,
            flightNumber = routeObject["flight_number"].toString(),
            airlineIata = routeObject["airline_iata"].toString(),
            depAirportIata = routeObject["dep_iata"].toString(),
            depTerminal = (routeObject["dep_terminals"] as JsonArray).first().toString(),
            depScheduled = getTime(routeObject["dep_time"].toString()),
            arrAirportIata = routeObject["arr_iata"].toString(),
            arrTerminal = (routeObject["arr_terminals"] as JsonArray).first().toString(),
            arrScheduled = getTime(routeObject["arr_time"].toString()),
            updated = getDateTime(routeObject["updated"].toString())
        )
        val operatingDays: List<String> = (routeObject["days"] as JsonArray).map{it.toString().trim('"')}       // days of week this flight operates, used to generate schedules

        // TODO: If the flight is operating today, then we retrieve delay information for the flight
        // TODO: insert conditional statement here
        // httpResponse = client.get("https://airlabs.co/api/v9/flight?api_key=$API_KEY_AIRLABS&flight_iata=$flightIata")

        // call.respondText(flightInfo.toString())
        call.respond(flightInfo)
    }

    get("/") {
        call.respond(200)
    }
}

// Function taking in a string with format '"HH:MM"' and converts it into a LocalDateTime for flightInfo
fun getTime(timeString: String): LocalDateTime {
    val time = LocalTime.parse(timeString.trim('"'), DateTimeFormatter.ofPattern("HH:mm"))
    return LocalDateTime.of(LocalDate.now(), time)
}

// Function taking in a string with format '"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"' and converts it into a LocalDateTime for flightInfo
fun getDateTime(dateTimeString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val instant = Instant.parse(dateTimeString.trim('"'))
    return instant.atZone(ZoneOffset.UTC).toLocalDateTime()
}