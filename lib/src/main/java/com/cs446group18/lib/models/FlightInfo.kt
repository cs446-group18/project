package com.cs446group18.lib.models

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

const val HOURS_IN_AIRPORT_DELAY_GRAPH = 4
sealed interface Cacheable

@Serializable
data class ScheduledFlight(
    val ident_iata: String, // e.g. AC8835
    val actual_ident_iata: String,
    val scheduled_in: Instant,
    val scheduled_out: Instant,
    val origin_iata: String,
    val destination_iata: String,
) {
    fun toFlightInfo(template: FlightInfo) = FlightInfo(
        ident_iata = ident_iata,
        flight_number = destructureFlightCode(ident_iata).component2(),
        status = "Scheduled",
        departure_delay_raw = 0,
        arrival_delay_raw = 0,
        scheduled_out = scheduled_out,
        estimated_out = scheduled_out,
        actual_out = null,
        scheduled_in = scheduled_in,
        estimated_in = scheduled_in,
        actual_in = null,
        operator_iata = destructureFlightCode(ident_iata).component1(),
        origin = template.origin,
        destination = template.destination,
        gate_origin = null,
        gate_destination = null,
        terminal_origin = null,
        terminal_destination = null,
    )
}

data class ScheduledFlightsResponse(
    val scheduled: List<ScheduledFlight>
): Cacheable

@Serializable
data class Airport(
    val code_iata: String,
    val name: String,
    val city: String,
    val timezone: String,
) : Cacheable {
    fun cleanName(): String {
        return name
            .replace(Regex("""\b(Int'l|Intl|International|Airport)\b"""), "")
            .trim()
    }
}

@Serializable
data class FlightInfo(
    val ident_iata: String, // e.g. AC8835
    val flight_number: String, // e.g. 8838
    val status: String,

    @SerialName("departure_delay")
    val departure_delay_raw: Int,
    @SerialName("arrival_delay")
    val arrival_delay_raw: Int,

    val scheduled_out: Instant,
    val estimated_out: Instant?,
    val actual_out: Instant?,
    val scheduled_in: Instant,
    val estimated_in: Instant?,
    val actual_in: Instant?,
    val operator_iata: String, // e.g. AC
    val origin: Airport,
    val destination: Airport,
    val gate_origin: String?,
    val gate_destination: String?,
    val terminal_origin: String?,
    val terminal_destination: String?,
) {
    fun getDepartureDate() = scheduled_out.toLocalDateTime(TimeZone.of(origin.timezone)).date
    fun getDuration(): Duration {
        return scheduled_in - scheduled_out
    }

    // TODO: default to (actual_out - scheduled_out)?
    fun getDepartureDelay(): Duration = departure_delay_raw.toDuration(DurationUnit.SECONDS)
    fun getArrivalDelay(): Duration = arrival_delay_raw.toDuration(DurationUnit.SECONDS)
}

@Serializable
data class FlightInfoResponse(
    val flights: List<FlightInfo>,
) : Cacheable

@Serializable
data class AirportDelayResponse(
    val departures: List<FlightInfo>,
) : Cacheable

@Serializable
data class AirportDelayWrapper(
    val response: AirportDelayResponse,
    val intervalStart: Instant,
    val intervalEnd: Instant,
) {
    fun getAverageDelays() : List<Int> {
        val flightsByHour = IntArray(HOURS_IN_AIRPORT_DELAY_GRAPH)
        val delaysByHour = IntArray(HOURS_IN_AIRPORT_DELAY_GRAPH)
        for(flight in response.departures) {
            val delay = flight.getDepartureDelay().inWholeMinutes.toInt()
            flight.actual_out ?: continue
            val hourIndex =
                ((intervalStart - flight.actual_out).absoluteValue.inWholeHours % HOURS_IN_AIRPORT_DELAY_GRAPH).toInt()
            flightsByHour[hourIndex]++
            if (delay > 0) {
                delaysByHour[hourIndex] += delay
            }
        }
        val res = IntArray(HOURS_IN_AIRPORT_DELAY_GRAPH)
        for (i in 0 until HOURS_IN_AIRPORT_DELAY_GRAPH) {
            res[i] = if (flightsByHour[i] > 0) delaysByHour[i] / flightsByHour[i] else 0
        }
        return res.toList()
    }
}

data class AmadeusDelayPrediction(
    var delayRate7: Int? = null,                // percentage of flights delayed in past 7 days (e.g. 14 for 14%)
    var delayRate14: Int? = null,               // percentage of flights delayed in past 14 days (e.g. 12 for 12%)
    var delayRate30: Int? = null,               // percentage of flights delayed in past 30 days (e.g. 10 for 10%)
    var avgDelay7: Int? = null,                 // the average length of all delays in past 7 days in minutes (e.g. 30)
    var avgDelay14: Int? = null,                // the average length of all delays in past 14 days in minutes (e.g. 24)
    var avgDelay30: Int? = null,                // the average length of all delays in past 30 days in minutes (e.g. 19)
)