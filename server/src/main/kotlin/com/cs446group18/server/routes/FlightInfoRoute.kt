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
    get("/oldFlightInfo/{flight_iata}") {    // TODO: add date parameter
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
            depScheduled = oldGetTime(parseElement(routeObject["dep_time"])),
            arrAirportIata = parseElement(routeObject["arr_iata"]),
            arrTerminal = parseElement((routeObject["arr_terminals"] as JsonArray).first()),
            arrScheduled = oldGetTime(parseElement(routeObject["arr_time"])),
        )

        val operatingDays: List<String> = (routeObject["days"] as JsonArray).map{it.toString().trim('"')}       // days of week this flight operates, used to generate schedules

        // If the date of the flight is today, retrieve delay information for the flight
        // TODO: insert conditional statement here to check date of flight
        httpResponse = client.get("https://airlabs.co/api/v9/flight?api_key=$API_KEY_AIRLABS&flight_iata=$flightIata")
        responseObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())
        val infoObject: JsonObject = responseObject["response"] as JsonObject

        flightInfo.depGate = parseElement(infoObject["dep_gate"])
        flightInfo.depEstimated = oldGetDateTime(parseElement(infoObject["dep_estimated"]))
        flightInfo.depActual = oldGetDateTime(parseElement(infoObject["dep_actual"]))
        flightInfo.arrGate = parseElement(infoObject["arr_gate"])
        flightInfo.arrEstimated = oldGetDateTime(parseElement(infoObject["arr_estimated"]))
        flightInfo.arrActual = oldGetDateTime(parseElement(infoObject["arr_actual"]))
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
        val formatter = DateTimeFormatter.ISO_DATE_TIME

        // Select today's flight
        val flightArray: JsonArray = responseObject["flights"] as JsonArray
        for (flightElement: JsonElement in flightArray){
            val flightObject = flightElement as JsonObject

            // TODO: ensure it properly fetches today's flight because of UTC time displayed in the response object
            val scheduledOutDate = LocalDateTime.parse(parseElement(flightObject["scheduled_out"]), formatter).toLocalDate()
            // TODO: if there is no flight today, fetch tomorrow's flight by default (or the next flight available, e.g. some routes run only on weekends)
            if (scheduledOutDate.equals(LocalDate.now())) { // Compare with today's date
                selectedFlight = flightObject
                break
            }
        }

        // TODO: search schedule for flights that are not today

        // Retrieve information for selected flight and map onto dataclass
        if (selectedFlight != null) {
            // Retrieve departure and arrival airport information from flight object; this info is nested in another object
            val departureInfo: JsonObject = selectedFlight["origin"] as JsonObject
            val arrivalInfo: JsonObject = selectedFlight["origin"] as JsonObject

            // Fetch departure and arrival times and convert them into local time
            val schDepUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["scheduled_out"]))    // scheduled departure in UTC
            val schArrUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["scheduled_in"]))     // scheduled arrival in UTC
            val estDepUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["estimated_out"]))    // estimated departure in UTC
            val estArrUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["estimated_in"]))     // estimated arrival in UTC
            val actDepUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["actual_out"]))       // actual departure in UTC
            val actArrUTC: LocalDateTime? = parseDateTime(parseElement(selectedFlight["actual_in"]))        // actual arrival in UTC

            val flightDuration: Int = Duration.between(schDepUTC, schArrUTC).toMinutes().toInt()
            val schDepLocal = convertUTCtoLocal(schDepUTC, parseElement(departureInfo["timezone"]))
            val schArrLocal = convertUTCtoLocal(schArrUTC, parseElement(arrivalInfo["timezone"]))


            // Map onto dataclass
            /**
            var flightInfo = FlightInfo(
                flightIata = flightIata!!,
            )
            */

            /**
            "ident": "JZA8838",
            "ident_icao": "JZA8838",
            "ident_iata": "QK8838",
            "fa_flight_id": "JZA8838-1679178960-schedule-0347",
            "operator": "JZA",
            "operator_icao": "JZA",
            "operator_iata": "QK",
            "flight_number": "8838",
            "registration": "C-FKJZ",
            "atc_ident": null,
            "inbound_fa_flight_id": "JZA8933-1679170819-airline-0070",
            "codeshares": [
                "TAP8030",
                "UAL8126",
                "ACA8838"
            ],
            "codeshares_iata": [
                "TP8030",
                "UA8126",
                "AC8838"
            ],
            "blocked": false,
            "diverted": false,
            "cancelled": false,
            "position_only": false,
            "origin": {
                "code": "CYYZ",
                "code_icao": "CYYZ",
                "code_iata": "YYZ",
                "code_lid": null,
                "timezone": "America/Toronto",
                "name": "Toronto Pearson Int'l",
                "city": "Toronto",
                "airport_info_url": "/airports/CYYZ"
            },
            "destination": {
                "code": "KRDU",
                "code_icao": "KRDU",
                "code_iata": "RDU",
                "code_lid": "RDU",
                "timezone": "America/New_York",
                "name": "Raleigh-Durham Intl",
                "city": "Raleigh/Durham",
                "airport_info_url": "/airports/KRDU"
            },
            "departure_delay": 0,
            "arrival_delay": 0,
            "filed_ete": 5220,
            "foresight_predictions_available": false,
            "scheduled_out": "2023-03-20T22:35:00Z",
            "estimated_out": "2023-03-20T22:35:00Z",
            "actual_out": null,
            "scheduled_off": "2023-03-20T22:45:00Z",
            "estimated_off": "2023-03-20T22:45:00Z",
            "actual_off": null,
            "scheduled_on": "2023-03-21T00:12:00Z",
            "estimated_on": "2023-03-21T00:12:00Z",
            "actual_on": null,
            "scheduled_in": "2023-03-21T00:22:00Z",
            "estimated_in": "2023-03-21T00:22:00Z",
            "actual_in": null,
            "progress_percent": 0,
            "status": "Scheduled",
            "aircraft_type": "CRJ9",
            "route_distance": 541,
            "filed_airspeed": 325,
            "filed_altitude": null,
            "route": null,
            "baggage_claim": null,
            "seats_cabin_business": 12,
            "seats_cabin_coach": 64,
            "seats_cabin_first": 0,
            "gate_origin": "F97",
            "gate_destination": "C12",
            "terminal_origin": "1",
            "terminal_destination": "2",
            "type": "Airline"
             */

            //2. delay information for today's flight


            //3. historical delay information (searching through and average), range of 10 days in the past


            //need to return today's flight, check todays date against response. keep in mind response is in UTC time, need to convert
        }

        val encodeDefaultJson = Json { encodeDefaults = true; isLenient = true}
        val responseJson = encodeDefaultJson.encodeToString(responseObject)

        call.respondText(responseJson, ContentType.Application.Json)
    }

    get("/") {
        call.respond(200)
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

// Function taking in a string with format '"HH:MM"' and converts it into a LocalDateTime for flightInfo
fun oldGetTime(timeString: String?): LocalDateTime? {
    if (timeString.isNullOrEmpty()) return null
    val time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"))
    return LocalDateTime.of(LocalDate.now(), time)
}

// Function taking in a string with format '"yyyy-MM-dd HH:mm"' and converts it into a LocalDateTime for flightInfo, used specifically for
fun oldGetDateTime(dateTimeString: String?): LocalDateTime? {
    if (dateTimeString.isNullOrEmpty() || dateTimeString == "null") return null
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return LocalDateTime.parse(dateTimeString, formatter)
}