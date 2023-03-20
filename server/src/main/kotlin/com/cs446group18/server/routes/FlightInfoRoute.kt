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
import java.time.*
import java.time.format.DateTimeFormatter

const val API_KEY_AIRLABS = "b0134401-3dd2-469c-85cf-4974afbf338b"

const val API_KEY_AERO = "Yyj2JugSpnL9ZUITA7QSTg4fkstUqsUK"

const val BASE_URL_AERO = "https://aeroapi.flightaware.com/aeroapi"

fun convertUtcToLocal(dateTimeString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val utcDateTime = LocalDateTime.parse(dateTimeString, formatter)
    val localZoneId = ZoneId.systemDefault()
    return utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZoneId).toLocalDateTime()
}

fun Route.flightInfo() {
    //route to aero api, calling aero api /flights, returns information about a single flight route for the past 12 days, including today and tomorrow
    get("/flightInfo/{flight_iata}"){
        val flightIata = call.parameters["flight_iata"]
        val client = HttpClient()
        var selectedFlight: JsonObject? = null

        // Fetch information from flight database, retrieves flights from today, tomorrow and 12 days into the past)
        var httpResponse = client.get("$BASE_URL_AERO/flights/$flightIata"){
            headers {
                append("x-apikey", API_KEY_AERO)
            }
        }
        var responseObject: JsonObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())

        // Parse through array of returned flights to find today's flight
        val flightArray: JsonArray = responseObject["flights"] as JsonArray
        for (flightElement: JsonElement in flightArray){
            val flightObject = flightElement as JsonObject

            // Convert scheduled departure date in UTC to local date based on timezone of departure airport
            val scheduledDepartureUTC: LocalDateTime? = parseDateTime(parseElement(flightObject["scheduled_out"]))    // scheduled departure in UTC
            val departureTimezone: String? = parseElement((flightObject["origin"] as JsonObject)["timezone"])
            val scheduledOutDate = convertUTCtoLocal(scheduledDepartureUTC, departureTimezone)?.toLocalDate()

            // Check if scheduled departure date is today
            if (scheduledOutDate!! == LocalDate.now()) { // Compare with today's date
                selectedFlight = flightObject
                break
            }
            // TODO: if there is no flight today, fetch tomorrow's flight by default (or the next flight available, e.g. some routes run only on weekends)
        }

        // TODO: search schedule for flights that are not today

        var flightInfo: FlightInfo? = null
        // Retrieve information for selected flight and map onto dataclass
        if (selectedFlight != null) {
            // Retrieve departure and arrival airport information from flight object; this info is nested in another object
            val departureInfo: JsonObject = selectedFlight["origin"] as JsonObject
            val arrivalInfo: JsonObject = selectedFlight["destination"] as JsonObject

            // Fetch departure and arrival times and convert them into local time
            val schDepUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["scheduled_out"]))    // scheduled departure in UTC
            val schArrUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["scheduled_in"]))     // scheduled arrival in UTC
            val estDepUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["estimated_out"]))    // estimated departure in UTC
            val estArrUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["estimated_in"]))     // estimated arrival in UTC
            val actDepUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["actual_out"]))       // actual departure in UTC
            val actArrUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["actual_in"]))        // actual arrival in UTC

            val schDepLocal: LocalDateTime? = convertUTCtoLocal(schDepUTC, parseElement(departureInfo["timezone"]))
            val schArrLocal: LocalDateTime? = convertUTCtoLocal(schArrUTC, parseElement(arrivalInfo["timezone"]))
            val estDepLocal: LocalDateTime? = convertUTCtoLocal(estDepUTC, parseElement(departureInfo["timezone"]))
            val estArrLocal: LocalDateTime? = convertUTCtoLocal(estArrUTC, parseElement(arrivalInfo["timezone"]))
            val actDepLocal: LocalDateTime? = convertUTCtoLocal(actDepUTC, parseElement(departureInfo["timezone"]))
            val actArrLocal: LocalDateTime? = convertUTCtoLocal(actArrUTC, parseElement(arrivalInfo["timezone"]))

            // Calculate flight duration, delay and status
            val flightDuration: Int = Duration.between(schDepUTC, schArrUTC).toMinutes().toInt()
            val flightProgress: Int? = parseElement(selectedFlight["progress_percent"])?.toInt()
            var delay: Int = 0

            var flightStatus: FlightStatus = FlightStatus.SCHEDULED
            if (parseElement(arrivalInfo["cancelled"]) == "true") flightStatus = FlightStatus.CANCELLED
            else if (parseElement(arrivalInfo["diverted"]) == "true") flightStatus = FlightStatus.DIVERTED

            if (flightProgress != null) {
                // Calculate delay: go by departure until the plane is in the air, then go by delay in arrival.
                delay = if (actArrLocal != null) Duration.between(schArrUTC, actArrUTC).toMinutes().toInt()
                else if (flightProgress in 1..99) Duration.between(schArrUTC, estArrUTC).toMinutes().toInt()
                else if (actDepLocal != null) Duration.between(schDepUTC, actDepUTC).toMinutes().toInt()
                else Duration.between(schDepUTC, estDepUTC).toMinutes().toInt()

                // Calculate status: flights are considered delayed only if delay is longer than 15 minutes
                if (flightProgress == 0 && (delay == null || delay > 15)) flightStatus = FlightStatus.DELAYED
                else if (flightProgress == 100) flightStatus = FlightStatus.LANDED
                else if (flightProgress in 1..99) flightStatus = FlightStatus.EN_ROUTE
            }

            // Fetch airline information (name is not returned in flight object)
            val airlineIata = parseElement(selectedFlight["flight_number"])?.let {
                flightIata?.dropLast(it.length)
            } ?: flightIata?.take(2)
            httpResponse = client.get("$BASE_URL_AERO/operators/$airlineIata"){
                headers {
                    append("x-apikey", API_KEY_AERO)
                }
            }
            val airlineObject: JsonObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())

            // Map onto FlightInfo dataclass
            flightInfo = FlightInfo(
                // Flight information
                flightIata = flightIata!!,
                flightNumber = parseElement(selectedFlight["flight_number"]),
                flightDuration = flightDuration,
                flightStatus = flightStatus,
                delay = delay,

                // Airline information
                airlineIata = airlineIata,
                airlineName = if (parseElement(airlineObject["shortname"]) != null) parseElement(airlineObject["shortname"])
                    else parseElement(airlineObject["name"]),

                // Departure airport information
                depAirportIata = parseElement(departureInfo["code_iata"]),
                depAirportName = cleanAirportName(parseElement(departureInfo["name"])),
                depTerminal = parseElement(selectedFlight["terminal_origin"]),
                depScheduled = schDepLocal,
                depEstimated = estDepLocal,
                depActual = actDepLocal,
                depGate = parseElement(selectedFlight["gate_origin"]),
                depCity = parseElement(departureInfo["city"]),
                // depCountry is not provided in flight info data, TODO: add if necessary from calling airports endpoints

                // Arrival airport information
                arrAirportIata = parseElement(arrivalInfo["code_iata"]),
                arrAirportName = cleanAirportName(parseElement(arrivalInfo["name"])),
                arrTerminal = parseElement(selectedFlight["terminal_destination"]),
                arrScheduled = schArrLocal,
                arrEstimated = estArrLocal,
                arrActual = actArrLocal,
                arrGate = parseElement(selectedFlight["gate_destination"]),
                arrCity = parseElement(arrivalInfo["city"]),
                // depCountry is not provided in flight info data, TODO: add if necessary from calling airports endpoints

                // TODO: Historical delay information

                // TODO: Amadeus delay prediction

                // TODO: Add list of flights scheduled on other days in next ~2 weeks or so for users to select

                // TODO: Related flights?
            )
        }

        val encodeDefaultJson = Json { encodeDefaults = true; isLenient = true}
        val responseJson = encodeDefaultJson.encodeToString(flightInfo)
        call.respondText(responseJson, ContentType.Application.Json)
    }
}

// Parses a JsonElement to trim double quote delimiters and properly store null values.
fun parseElement(element: JsonElement?): String? {
    if (element is JsonNull) return null
    val strElement: String = element.toString().trim('"')
    return if (strElement == "null" || strElement.isNullOrEmpty()) null else strElement
}

// Converts a string representing a time with format "yyyy-MM-dd'T'HH:mm:ss'Z'" into a LocalDateTime object.
fun parseDateTime(dateTimeStr: String?): LocalDateTime? {
    if (dateTimeStr.isNullOrEmpty() || dateTimeStr == "null") return null
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return LocalDateTime.parse(dateTimeStr, formatter)
}

// Converts a LocalDateTime in UTC to equivalent time in a given timezone (TZ database format).
fun convertUTCtoLocal(timeInUTC: LocalDateTime?, timezone: String?): LocalDateTime? {
    if (timezone.isNullOrEmpty() || timeInUTC == null) return null
    return timeInUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime()
}

// Cleans an airport name to remove "Int'l", "Intl", "International", and "Airport" in its name.
fun cleanAirportName(airportName: String?): String? {
    if (airportName == null) return null
    return airportName
        .replace(Regex("""\b(Int'l|Intl|International|Airport)\b"""), "")
        .trim()
}