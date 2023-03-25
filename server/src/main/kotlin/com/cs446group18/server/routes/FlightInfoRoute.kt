package com.cs446group18.server.routes

import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.FlightStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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

// Route to Aero API. Given IATA code and optionally date in ISO 8601 format, returns information for the flight + historical data + prediction.
fun Route.flightInfo() {
    get("/flightInfo/{flight_iata}") {
        // Retrieve path parameter (IATA code) and optional query parameters from request (date).
        val flightIata: String? = call.parameters["flight_iata"]
        val airlineIata: String? = flightIata?.take(2)
        val flightNumber = flightIata?.substring(2)
        val dateParam = call.parameters["date"]
        val selectedDate: LocalDate? = if (dateParam != null) LocalDate.parse(dateParam, DateTimeFormatter.ISO_LOCAL_DATE) else null


        // We must fetch information from both flights database and schedules database to fill all necessary parameters in return object.
        val client = HttpClient()
        var responseObject: JsonObject

        // Fetch information from flight database. Retrieves flights from today, tomorrow and 12 days into the past.
        var httpResponse: HttpResponse = client.get("$BASE_URL_AERO/flights/$flightIata") {
            headers { append("x-apikey", API_KEY_AERO) }
        }
        responseObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())
        val flightArray: JsonArray = responseObject["flights"] as JsonArray     // Json Array containing returned flights from flights DB.

        // TODO: Add check to ensure previous fetch was successful before attempting to find future flights.
        // If previous fetch was successful, fetch information from schedules database. Retrieves scheduled flights for the next 2 weeks.
        val scheduleRequest = "$BASE_URL_AERO/schedules/${LocalDate.now()}/${LocalDate.now().plusDays(15)}?airline=$airlineIata&flight_number=$flightNumber"
        httpResponse = client.get(scheduleRequest) {
            headers { append("x-apikey", API_KEY_AERO) }
        }
        responseObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())
        val scheduleArray: JsonArray = responseObject["scheduled"] as JsonArray   // Json Array containing returned flights from schedules DB.

        // Fetch airline information (name is not returned in flight object).
        httpResponse = client.get("$BASE_URL_AERO/operators/$airlineIata") {
            headers {
                append("x-apikey", API_KEY_AERO)
            }
        }
        val airlineObject: JsonObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())


        // Parse through returned flights to select flight with provided date in flight DB and schedule DB return arrays.
        // If no date (null) or invalid date is provided, select today's flight, or if there was no flight today, return next scheduled flight.
        var selectedFlight: JsonObject? = null
        var departureInfo: JsonObject? = null
        var arrivalInfo: JsonObject? = null

        // Search flight database return array for selected flight.
        for (flightElement: JsonElement in flightArray) {
            val flightObject = flightElement as JsonObject
            val departureDateUTC: LocalDateTime? = parseDateTime(parseElement(flightObject["scheduled_out"]))    // scheduled departure in UTC

            // Retrieve departure and arrival airport information from flight object; this info is nested in another object
            if (departureInfo == null || arrivalInfo == null) {
                departureInfo = flightObject["origin"] as JsonObject
                arrivalInfo = flightObject["destination"] as JsonObject
            }

            // If date was not specified: we select the flight that departs (1) later than 6 hours in the past and (2) is the earliest such flight.
            if (selectedDate == null) {
                if (selectedFlight == null) selectedFlight = flightObject
                else if (departureDateUTC?.isAfter(LocalDateTime.now(ZoneOffset.UTC).minusHours(6))!!) {
                    val selectedDepartureUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["scheduled_out"]))
                    if (departureDateUTC.isBefore(selectedDepartureUTC)) {
                        selectedFlight = flightObject
                    }
                }
            }

            // If date was specified, check if departure date matches the selected date.
            else {
                // Convert scheduled departure date in UTC to local date based on timezone of departure airport.
                val departureDate = convertUTCtoLocal(departureDateUTC, parseElement(departureInfo["timezone"]))?.toLocalDate()
                if (selectedDate == departureDate!!) {
                    selectedFlight = flightObject
                    break
                }
            }
        }

        // Search schedule database return array for selected flight only if selected flight was not already found or no date was specified.
        if (selectedFlight == null || selectedDate == null) {
            for (scheduleElement: JsonElement in scheduleArray) {
                val scheduleObject = scheduleElement as JsonObject
                val departureDateUTC: LocalDateTime? = parseDateTime(parseElement(scheduleObject["scheduled_out"]))    // scheduled departure in UTC

                // If date was not specified: we select the flight that departs (1) later than 6 hours in the past and (2) is the earliest such flight.
                if (selectedDate == null) {
                    if (selectedFlight == null) selectedFlight = scheduleObject
                    else if (departureDateUTC?.isAfter(LocalDateTime.now(ZoneOffset.UTC).minusHours(6))!!) {
                        val selectedDepartureUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["scheduled_out"]))
                        if (departureDateUTC.isBefore(selectedDepartureUTC)) { selectedFlight = scheduleObject
                        }
                    }
                }

                // If date was specified, check if departure date matches the selected date.
                else {
                    // If no results were returned in flights DB search, we need to find departure and arrival information again - this will be seldom used.
                    if (departureInfo == null) {
                        httpResponse = client.get("$BASE_URL_AERO/airports/${parseElement(scheduleObject["origin_iata"])}") {
                            headers {
                                append("x-apikey", API_KEY_AERO)
                            }
                        }
                        departureInfo = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())
                    }
                    if (arrivalInfo == null) {
                        httpResponse = client.get("$BASE_URL_AERO/airports/${parseElement(scheduleObject["destination_iata"])}") {
                            headers {
                                append("x-apikey", API_KEY_AERO)
                            }
                        }
                        arrivalInfo = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())
                    }
                    val departureDate = convertUTCtoLocal(departureDateUTC, parseElement(departureInfo["timezone"]))?.toLocalDate()
                    if (selectedDate == departureDate!!) {
                        selectedFlight = scheduleObject
                        break
                    }
                }
            }
        }


        // Retrieve information for selected flight and map onto dataclass if flight was from flights DB (live + historical flights).
        var flightInfo: FlightInfo? = null
        if (selectedFlight != null && departureInfo != null && arrivalInfo != null) {
            if (selectedFlight.containsKey("progress_percent")) {
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
                var delay = 0

                var flightStatus: FlightStatus = FlightStatus.SCHEDULED
                if (parseElement(selectedFlight["cancelled"]) == "true") flightStatus = FlightStatus.CANCELLED
                else if (parseElement(selectedFlight["diverted"]) == "true") flightStatus = FlightStatus.DIVERTED

                if (flightProgress != null) {
                    // Calculate delay: go by departure until the plane is in the air, then go by delay in arrival.
                    delay =
                        if (actArrLocal != null) Duration.between(schArrUTC, actArrUTC).toMinutes().toInt()
                        else if (flightProgress in 1..99) Duration.between(schArrUTC, estArrUTC).toMinutes().toInt()
                        else if (actDepLocal != null) Duration.between(schDepUTC, actDepUTC).toMinutes().toInt()
                        else Duration.between(schDepUTC, estDepUTC).toMinutes().toInt()

                    // Calculate status: flights are considered delayed only if delay is longer than 15 minutes
                    if (flightProgress == 0 && (delay == null || delay > 15)) flightStatus = FlightStatus.DELAYED
                    else if (flightProgress == 100) flightStatus = FlightStatus.LANDED
                    else if (flightProgress in 1..99) flightStatus = FlightStatus.EN_ROUTE
                }

                // Map onto FlightInfo dataclass
                flightInfo = FlightInfo(
                    // Flight information
                    flightIata = flightIata!!,
                    flightNumber = flightNumber,
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

                    // Arrival airport information
                    arrAirportIata = parseElement(arrivalInfo["code_iata"]),
                    arrAirportName = cleanAirportName(parseElement(arrivalInfo["name"])),
                    arrTerminal = parseElement(selectedFlight["terminal_destination"]),
                    arrScheduled = schArrLocal,
                    arrEstimated = estArrLocal,
                    arrActual = actArrLocal,
                    arrGate = parseElement(selectedFlight["gate_destination"]),
                    arrCity = parseElement(arrivalInfo["city"]),

                    // TODO: Historical delay information

                    // TODO: Amadeus delay prediction

                    // TODO: Add list of flights scheduled on other days in next ~2 weeks or so for users to select
                )
            }

            // Retrieve information for selected flight and map onto dataclass if flight was from schedules DB (future flights).
            else if (selectedFlight != null) {
                // Fetch departure and arrival times, convert them into local time, and calculate flight duration.
                val schDepUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["scheduled_out"]))    // scheduled departure in UTC
                val schArrUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["scheduled_in"]))     // scheduled arrival in UTC
                val schDepLocal: LocalDateTime? = convertUTCtoLocal(schDepUTC, parseElement(departureInfo["timezone"]))
                val schArrLocal: LocalDateTime? = convertUTCtoLocal(schArrUTC, parseElement(arrivalInfo["timezone"]))
                val flightDuration: Int = Duration.between(schDepUTC, schArrUTC).toMinutes().toInt()
                var flightStatus: FlightStatus = FlightStatus.SCHEDULED

                flightInfo = FlightInfo(
                    // Flight information
                    flightIata = flightIata!!,
                    flightNumber = flightNumber,
                    flightDuration = flightDuration,
                    flightStatus = flightStatus,

                    // Airline information
                    airlineIata = airlineIata,
                    airlineName = if (parseElement(airlineObject["shortname"]) != null) parseElement(airlineObject["shortname"])
                        else parseElement(airlineObject["name"]),

                    // Departure airport information
                    depAirportIata = parseElement(departureInfo["code_iata"]),
                    depAirportName = cleanAirportName(parseElement(departureInfo["name"])),
                    depScheduled = schDepLocal,
                    depCity = parseElement(departureInfo["city"]),
                    depCountry = parseElement(departureInfo["country_code"]),

                    // Arrival airport information
                    arrAirportIata = parseElement(arrivalInfo["code_iata"]),
                    arrAirportName = cleanAirportName(parseElement(arrivalInfo["name"])),
                    arrScheduled = schArrLocal,
                    arrCity = parseElement(arrivalInfo["city"]),
                    arrCountry = parseElement(arrivalInfo["country_code"])

                    // TODO: Historical delay information

                    // TODO: Amadeus delay prediction

                    // TODO: Add list of flights scheduled on other days in next ~2 weeks or so for users to select
                )
            }
        }


        val encodeDefaultJson = Json { encodeDefaults = true; isLenient = true }
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