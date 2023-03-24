package com.cs446group18.server.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

//Wen's api key
const val API_KEY_AERO_2 = "lY2EJ3AcgGkFcpjJ5CgKWYrZDGy211A5"

const val HOURS_AGO = 4

fun Route.airportInfo() {
    get("/airportDelay/{airport_code}") {

        var airportCode = call.parameters["airport_code"]
        var client = HttpClient()

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS) // current time rounded down to the closest hour
        val startTime = now.minusHours(HOURS_AGO.toLong()) // time $HOURS_AGO hours ago from current time

        var httpResponse = client.get("$BASE_URL_AERO/airports/$airportCode/flights/departures"){
            headers {
                append("x-apikey", API_KEY_AERO_2)
                parameter("start", startTime)
                parameter("end", now)
                parameter("max_pages", 5)
            }
        }
        var responseObject: JsonObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())

        //go through each response object in depature and calculate the average delay of each flight. dont count early flights as delay
        val delaysByHour = IntArray(HOURS_AGO) // to keep track of total delay for each hour in the last 8 hours
        val flightsByHour = IntArray(HOURS_AGO) // to keep track of number of flights for each hour in the last 8 hours
        val departures = responseObject["departures"]?.jsonArray

        if (departures != null) {
            for (departure in departures) {
                val scheduledOutStr = departure.jsonObject["scheduled_out"]?.jsonPrimitive?.content
                val actualOutStr = departure.jsonObject["actual_out"]?.jsonPrimitive?.content
                if (scheduledOutStr != "null" && actualOutStr != "null") {
                    val scheduledOut = LocalDateTime.parse(scheduledOutStr, DateTimeFormatter.ISO_DATE_TIME)
                    val actualOut = LocalDateTime.parse(actualOutStr, DateTimeFormatter.ISO_DATE_TIME)
                    val delayString = (departure as JsonObject)["departure_delay"]?.jsonPrimitive?.content
                    val delay = if (delayString != "null") {
                        delayString!!.toInt() / 60 // convert seconds to minutes
                    } else {
                        Duration.between(scheduledOut, actualOut).toMinutes().toInt()
                    }
                    val hourIndex = (Duration.between(startTime, actualOut).toHours() % HOURS_AGO).toInt()
                    flightsByHour[hourIndex]++
                    if (delay > 0) {
                        delaysByHour[hourIndex] += delay
                    }
                }else{
                    continue
                }
            }
        }

        val avgDelaysByHour = IntArray(HOURS_AGO)

        for (i in 0 until HOURS_AGO) {
            val hourStart = startTime.plusHours(i.toLong())
            val hourEnd = startTime.plusHours((i + 1).toLong())
            val dateRange = "${hourStart.format(DateTimeFormatter.ISO_LOCAL_TIME)}-${hourEnd.format(DateTimeFormatter.ISO_LOCAL_TIME)}"
            //Uncomment for debugging
            // println("for time ${dateRange} delay is mins delaysByHour[$i] is ${delaysByHour[i]} flightsByHour[$i] is ${flightsByHour[i]}")
            val hourDelay = if (flightsByHour[i] > 0) delaysByHour[i] / flightsByHour[i] else 0
            avgDelaysByHour[i] = hourDelay
        }

        for (d in avgDelaysByHour){
            println(d)
        }

        client.close()
        call.respond(avgDelaysByHour)
    }
}