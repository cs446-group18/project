package com.cs446group18.lib.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.descriptors.*
import java.time.format.DateTimeFormatter

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private var formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override var descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}

enum class FlightStatus {
    SCHEDULED,
    LANDED,
    CANCELLED,
    EN_ROUTE,
    DELAYED,
    UNKNOWN
}
@Serializable
data class FlightInfo(
    // Flight information
    val flightIata: String?,                    // flight IATA code; airline IATA + flight number (e.g. AC8835)
    var flightDuration: Int? = null,            // flight duration in minutes (e.g. 120 for 2 hours)
    var flightStatus: FlightStatus? = null,     // status from FlightStatus enum class above
    val flightNumber: String?,                  // flight number (e.g. 8838, note this is not always only numbers hence String type)
    val airlineIata: String?,                   // airline IATA code (e.g. AC for Air Canada)
    var airlineName: String? = null,            // airline name (e.g. "Air Canada")
    var delay: Int? = null,                     // active delay in minutes

    // Departure airport information
    val depAirportIata: String?,                // IATA code of departure airport (e.g. YYZ for Toronto Pearson)
    var depAirportName: String? = null,         // name of departure airport (e.g. "Toronto Pearson International Airport")
    var depTerminal: String?,                   // terminal in departure airport (e.g. "1" for Terminal 1 in YYZ)
    var depGate: String? = null,                // gate in departure airport (e.g. "F64" in YYZ)
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual val depScheduled: LocalDateTime?,           // original scheduled departure time in local timezone (e.g. 18:35)
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual var depEstimated: LocalDateTime? = null,    // estimated departure time accounting for delays (e.g. 19:05)
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual var depActual: LocalDateTime? = null,       // actual departure time if plane has taken off (e.g. 19:12)
    var depCity: String? = null,                // city of departure (e.g. "Toronto")
    var depCountry: String? = null,             // country of departure (e.g. "Canada")

    // Arrival airport information
    val arrAirportIata: String?,                // IATA code of arrival airport (e.g. RDU for Raleigh-Durham)
    var arrAirportName: String? = null,         // name of arrival airport (e.g. "Raleigh-Durham International Airport")
    var arrTerminal: String?,                   // terminal in arrival airport (e.g. "2" for Terminal 2 in RDU)
    var arrGate: String? = null,                // gate in arrival airport (e.g. "C12" in RDU)
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual val arrScheduled: LocalDateTime?,           // original arrival departure time (e.g. 20:22)
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual var arrEstimated: LocalDateTime? = null,    // estimated arrival time accounting for delays (e.g. 20:43)
    @Serializable(with = LocalDateTimeSerializer::class)
    @Contextual var arrActual: LocalDateTime? = null,       // actual arrival time if plane has taken off (e.g. 20:46)
    var arrCity: String? = null,                // city of arrival (e.g. "Raleigh-Durham")
    var arrCountry: String? = null,             // country of arrival (e.g. "USA")

    // Historical delay information
    var delayRate7: Int? = null,                // percentage of flights delayed in past 7 days (e.g. 14 for 14%)
    var delayRate14: Int? = null,               // percentage of flights delayed in past 14 days (e.g. 12 for 12%)
    var delayRate30: Int? = null,               // percentage of flights delayed in past 30 days (e.g. 10 for 10%)
    var avgDelay7: Int? = null,                 // the average length of all delays in past 7 days in minutes (e.g. 30)
    var avgDelay14: Int? = null,                // the average length of all delays in past 14 days in minutes (e.g. 24)
    var avgDelay30: Int? = null,                // the average length of all delays in past 30 days in minutes (e.g. 19)
    // may add a dropdown to show these delayed flights in past 7 days and exact delay time

    // TODO: Delay predictions for scheduled flights
    // var predicted_delay: String?        // predicted range of delay using Amadeus
    // other parameters may be necessary such as percentage likelihood for this range

    // TODO: Related flights
    // we can provide a list of other flights operating this route on this day so users can see alternatives
)
