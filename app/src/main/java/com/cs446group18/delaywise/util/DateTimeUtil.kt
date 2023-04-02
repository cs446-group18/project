package com.cs446group18.delaywise.util

import kotlinx.datetime.*
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

val dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
val shortDateFormatter = DateTimeFormatter.ofPattern("MM-dd")

fun Instant.formatAsDate(timeZone: TimeZone = TimeZone.currentSystemDefault()) : String {
    return dateFormatter.format(this.toLocalDateTime(timeZone).toJavaLocalDateTime())
}

fun Instant.formatAsTime(timeZone: TimeZone = TimeZone.currentSystemDefault()) : String {
    return timeFormatter.format(this.toLocalDateTime(timeZone).toJavaLocalDateTime())
}

fun Instant.formatAsShortDate(timeZone: TimeZone = TimeZone.currentSystemDefault()) : String {
    return shortDateFormatter.format(this.toLocalDateTime(timeZone).toJavaLocalDateTime())
}

fun Duration.formatInHoursMinutes(): String {
    val minutes = this.inWholeMinutes
    if (minutes >= 60) {
        return "${minutes/60} hours, ${minutes%60} mins"
    }
    return "$minutes mins"
}
