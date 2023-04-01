package com.cs446group18.lib.models

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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