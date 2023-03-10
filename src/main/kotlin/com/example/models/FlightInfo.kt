package com.example.models

import java.time.LocalDateTime

enum class FlightStatus {
    SCHEDULED,
    LANDED,
    CANCELLED,
    EN_ROUTE,
    DELAYED
}

data class FlightInfo(
    // Flight information
    val flightIata: String,                     // flight IATA code; airline IATA + flight number (e.g. AC8835)
    val flightDuration: Int? = null,            // flight duration in minutes (e.g. 120 for 2 hours)
    val flightStatus: FlightStatus? = null,     // status from FlightStatus enum class above
    val flightNumber: String,                   // flight number (e.g. 8838, note this is not always only numbers hence String type)
    val airlineIata: String,                    // airline IATA code (e.g. AC for Air Canada)
    val airlineName: String? = null,            // airline name (e.g. "Air Canada")

    // Departure airport information
    val depAirportIata: String,                 // IATA code of departure airport (e.g. YYZ for Toronto Pearson)
    val depAirportName: String? = null,         // name of departure airport (e.g. "Toronto Pearson International Airport")
    val depTerminal: String?,                   // terminal in departure airport (e.g. "1" for Terminal 1 in YYZ)
    val depGate: String? = null,                // gate in departure airport (e.g. "F64" in YYZ)
    val depScheduled: LocalDateTime,            // original scheduled departure time in local timezone (e.g. 18:35)
    val depEstimated: LocalDateTime? = null,    // estimated departure time accounting for delays (e.g. 19:05)
    val depActual: LocalDateTime? = null,       // actual departure time if plane has taken off (e.g. 19:12)
    val depCity: String? = null,                // city of departure (e.g. "Toronto")
    val depCountry: String? = null,             // country of departure (e.g. "Canada")

    // Arrival airport information
    val arrAirportIata: String,                 // IATA code of arrival airport (e.g. RDU for Raleigh-Durham)
    val arrAirportName: String? = null,         // name of arrival airport (e.g. "Raleigh-Durham International Airport")
    val arrTerminal: String?,                   // terminal in arrival airport (e.g. "2" for Terminal 2 in RDU)
    val arrGate: String? = null,                // gate in arrival airport (e.g. "C12" in RDU)
    val arrScheduled: LocalDateTime,            // original arrival departure time (e.g. 20:22)
    val arrEstimated: LocalDateTime? = null,    // estimated arrival time accounting for delays (e.g. 20:43)
    val arrActual: LocalDateTime? = null,       // actual arrival time if plane has taken off (e.g. 20:46)
    val arrCity: String? = null,                // city of arrival (e.g. "Raleigh-Durham")
    val arrCountry: String? = null,             // country of arrival (e.g. "USA")

    // Historical delay information
    val delayRate7: Int? = null,                // percentage of flights delayed in past 7 days (e.g. 14 for 14%)
    val delayRate14: Int? = null,               // percentage of flights delayed in past 14 days (e.g. 12 for 12%)
    val delayRate30: Int? = null,               // percentage of flights delayed in past 30 days (e.g. 10 for 10%)
    val avgDelay7: Int? = null,                 // the average length of all delays in past 7 days in minutes (e.g. 30)
    val avgDelay14: Int? = null,                // the average length of all delays in past 14 days in minutes (e.g. 24)
    val avgDelay30: Int? = null,                // the average length of all delays in past 30 days in minutes (e.g. 19)
    // may add a dropdown to show these delayed flights in past 7 days and exact delay time

    // TODO: Delay predictions for scheduled flights
    // val predicted_delay: String?        // predicted range of delay using Amadeus
    // other parameters may be necessary such as percentage likelihood for this range

    // TODO: Related flights
    // we can provide a list of other flights operating this route on this day so users can see alternatives

    // Internal information for debugging
    val updated: LocalDateTime? = null          // date information was last updated, for identifying outdated info
)