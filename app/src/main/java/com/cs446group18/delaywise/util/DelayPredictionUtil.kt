package com.cs446group18.delaywise.util

import com.cs446group18.lib.models.FlightInfo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.time.Duration

fun Double.formatAsPercentString() : String {
    val ansInt = (this * 100).roundToInt()

    return "${ansInt}%"
}

val graphDateFormatter = DateTimeFormatter.ofPattern("MM-dd")

fun List<FlightInfo>.getGraphInputs() : Pair<List<String>,List<Int>> {
    val datesXAxis = this.filter{
        !it.cancelled && (it.getDepartureDelay() != Duration.ZERO) && it.getDepartureDate() != null
    }.map{
        it.getDepartureDate().toJavaLocalDate().format(graphDateFormatter)
    }

    val minDelayYAxis = this.filter{
        !it.cancelled && (it.getDepartureDelay() != Duration.ZERO)
    }.map{
        it.getDepartureDelay().inWholeMinutes.toInt()
    }

    return Pair(datesXAxis,minDelayYAxis)
}

fun List<FlightInfo>.getDelayPrediction() : List<String> {
    var sumOfDelay = 0
    var numDelay = 0
    var numCancelled = 0

    for (flightInfo in this) {
        if (flightInfo.cancelled) {
            numCancelled += 1
        }
        else if (flightInfo.getDepartureDelay() != Duration.ZERO) {
            numDelay += 1
            sumOfDelay += flightInfo.getDepartureDelay().inWholeMinutes.toInt()
        }
    }

    val rateDelay = numDelay.toDouble() / this.size
    val avgDelay = if (numDelay == 0) 0 else (sumOfDelay.toDouble()/numDelay).roundToInt()
    val rateCancelled = numCancelled.toDouble() / this.size

    var avgDelayString = ""
    if (avgDelay < 59) {
        avgDelayString = "$avgDelay min"
    }
    else {
        val hours = avgDelay / 60
        val minutes = avgDelay % 60

        avgDelayString = "${hours}h"
        if (minutes != 0) {
            avgDelayString += "${minutes}min"
        }
    }

    return listOf(rateDelay.formatAsPercentString(), "$avgDelayString", rateCancelled.formatAsPercentString())
}