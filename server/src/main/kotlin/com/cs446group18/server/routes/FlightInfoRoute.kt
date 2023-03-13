package com.cs446group18.server.routes

import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.FlightStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val API_KEY_AIRLABS = "b0134401-3dd2-469c-85cf-4974afbf338b"

fun Route.flightInfo() {
    get("/flightInfo/{flight_iata}") {    // TODO: add date parameter
        // val flightDate = call.parameters["date"]
        val flightIata = call.parameters["flight_iata"]
        val client = HttpClient()

        // Make API call to AirLabs Route DB for route information
        var httpResponse = client.get("https://airlabs.co/api/v9/routes?api_key=$API_KEY_AIRLABS&flight_iata=$flightIata")
        var responseObject: JsonObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())

        //Check if iata_code is valid
        val resp :JsonArray = responseObject["response"] as JsonArray
        if (resp.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Iata Code is Invalid")
        }

        val routeObject: JsonObject = resp.first() as JsonObject

        // Retrieve flight info fields from route Json object to create data class
        var flightInfo = FlightInfo(
            flightIata = flightIata!!,
            flightNumber = parseElement(routeObject["flight_number"]),
            flightDuration = parseElement(routeObject["duration"])?.toInt(),
            airlineIata = parseElement(routeObject["airline_iata"]),
            depAirportIata = parseElement(routeObject["dep_iata"]),
            depTerminal = parseElement((routeObject["dep_terminals"] as JsonArray).first()),
            depScheduled = getTime(parseElement(routeObject["dep_time"])),
            arrAirportIata = parseElement(routeObject["arr_iata"]),
            arrTerminal = parseElement((routeObject["arr_terminals"] as JsonArray).first()),
            arrScheduled = getTime(parseElement(routeObject["arr_time"])),
        )

        val operatingDays: List<String> = (routeObject["days"] as JsonArray).map{it.toString().trim('"')}       // days of week this flight operates, used to generate schedules

        // If the date of the flight is today, retrieve delay information for the flight
        // TODO: insert conditional statement here to check date of flight
        httpResponse = client.get("https://airlabs.co/api/v9/flight?api_key=$API_KEY_AIRLABS&flight_iata=$flightIata")
        responseObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())
        val infoObject: JsonObject = responseObject["response"] as JsonObject

        flightInfo.depGate = parseElement(infoObject["dep_gate"])
        flightInfo.depEstimated = getDateTime(parseElement(infoObject["dep_estimated"]))
        flightInfo.depActual = getDateTime(parseElement(infoObject["dep_actual"]))
        flightInfo.arrGate = parseElement(infoObject["arr_gate"])
        flightInfo.arrEstimated = getDateTime(parseElement(infoObject["arr_estimated"]))
        flightInfo.arrActual = getDateTime(parseElement(infoObject["arr_actual"]))
        flightInfo.flightStatus = when (parseElement(infoObject["status"])) {
            "scheduled" -> FlightStatus.SCHEDULED
            "en-route" -> FlightStatus.EN_ROUTE
            "landed" -> FlightStatus.LANDED
            "canceled" -> FlightStatus.CANCELLED
            else -> FlightStatus.UNKNOWN
        }
        if (flightInfo.depEstimated != null && Duration.between(flightInfo.depScheduled, flightInfo.depEstimated).toMinutes() >= 15.0 && flightInfo.flightStatus == FlightStatus.SCHEDULED) flightInfo.flightStatus = FlightStatus.DELAYED

        flightInfo.depAirportName = parseElement(infoObject["dep_name"])
        flightInfo.depCity = parseElement(infoObject["dep_city"])
        flightInfo.depCountry = parseElement(infoObject["dep_country"])
        flightInfo.arrAirportName = parseElement(infoObject["arr_name"])
        flightInfo.arrCity = parseElement(infoObject["arr_city"])
        flightInfo.arrCountry = parseElement(infoObject["arr_country"])
        flightInfo.airlineName = parseElement(infoObject["airline_name"])
        flightInfo.delay = parseElement(infoObject["delayed"])?.toInt()

        val encodeDefaultJson = Json { encodeDefaults = true; isLenient = true}
        val responseJson = encodeDefaultJson.encodeToString(flightInfo)
        call.respondText(responseJson, ContentType.Application.Json)
    }

    get("/") {
        call.respond(200)
    }
}

fun parseElement(element: JsonElement?): String? {
    if (element is JsonNull) return null
    val strElement: String = element.toString().trim('"')
    return if (strElement == "null" || strElement.isNullOrEmpty()) null else strElement
}

// Function taking in a string with format '"HH:MM"' and converts it into a LocalDateTime for flightInfo
fun getTime(timeString: String?): LocalDateTime? {
    if (timeString.isNullOrEmpty()) return null
    val time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"))
    return LocalDateTime.of(LocalDate.now(), time)
}


// Function taking in a string with format '"yyyy-MM-dd HH:mm"' and converts it into a LocalDateTime for flightInfo, used specifically for
fun getDateTime(dateTimeString: String?): LocalDateTime? {
    if (dateTimeString.isNullOrEmpty() || dateTimeString == "null") return null
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return LocalDateTime.parse(dateTimeString, formatter)
}