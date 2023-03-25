package com.cs446group18.lib.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Serializable
data class Airport(
    var code_iata: String,
    var name: String,
    var city: String,
    var timezone: String,
) {
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
    val estimated_out: Instant,
    val actual_out: Instant?,
    val scheduled_in: Instant,
    val estimated_in: Instant,
    val actual_in: Instant?,
    val operator_iata: String, // e.g. AC
    val origin: Airport,
    val destination: Airport,
    val gate_origin: String?,
    val gate_destination: String?,
    val terminal_origin: String,
    val terminal_destination: String,
) {
    fun getDuration(): Duration {
        return scheduled_in - scheduled_out
    }
    fun getDepartureDelay(): Duration = departure_delay_raw.toDuration(DurationUnit.MINUTES)
    fun getArrivalDelay(): Duration = arrival_delay_raw.toDuration(DurationUnit.MINUTES)
}

@Serializable
data class FlightInfoResponse(
    val flights: List<FlightInfo>,
) : Cacheable()

data class AmadeusDelayPrediction(
    var delayRate7: Int? = null,                // percentage of flights delayed in past 7 days (e.g. 14 for 14%)
    var delayRate14: Int? = null,               // percentage of flights delayed in past 14 days (e.g. 12 for 12%)
    var delayRate30: Int? = null,               // percentage of flights delayed in past 30 days (e.g. 10 for 10%)
    var avgDelay7: Int? = null,                 // the average length of all delays in past 7 days in minutes (e.g. 30)
    var avgDelay14: Int? = null,                // the average length of all delays in past 14 days in minutes (e.g. 24)
    var avgDelay30: Int? = null,                // the average length of all delays in past 30 days in minutes (e.g. 19)
)