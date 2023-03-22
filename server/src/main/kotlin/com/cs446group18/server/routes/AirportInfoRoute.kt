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

fun Route.airportInfo() {
    get("/airport/{airport_code}") {
        println("hit /airport/{airport_code}")
        var airportCode = call.parameters["airport_code"]
        println("airportCode is $airportCode")
        var client = HttpClient()

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS) // current time rounded down to the closest hour
        val eightHoursAgo = now.minusHours(8) // time 8 hours ago from current time

        var httpResponse = client.get("$BASE_URL_AERO/airports/$airportCode/flights/departures"){
            headers {
                append("x-apikey", API_KEY_AERO_2)
                parameter("start", eightHoursAgo)
                parameter("end", now)
            }
        }
        var responseObject: JsonObject = Json.decodeFromString(JsonObject.serializer(), httpResponse.body())

        //go through each response object in depature and calculate the average delay of each flight. dont count early flights as delay
        //we can read the depature_delay and average it out

        val delaysByHour = IntArray(8) // to keep track of total delay for each hour in the last 8 hours
        val flightsByHour = IntArray(8) // to keep track of number of flights for each hour in the last 8 hours

        val departures = responseObject["departures"]?.jsonArray


        if (departures != null) {
            for (departure in departures) {
                println(departure.toString())
                //it fails because these 2 can be null, how to prevent error?
                val scheduledOutStr = departure.jsonObject["scheduled_out"]?.jsonPrimitive?.content
                val actualOutStr = departure.jsonObject["actual_out"]?.jsonPrimitive?.content
//                println("scheduledOutStr is ${scheduledOutStr.toString()}")
//                println("actualOutStr is ${actualOutStr.toString()}")
                if (scheduledOutStr != "null" && actualOutStr != "null") {
                    val scheduledOut = LocalDateTime.parse(scheduledOutStr, DateTimeFormatter.ISO_DATE_TIME)
                    val actualOut = LocalDateTime.parse(actualOutStr, DateTimeFormatter.ISO_DATE_TIME)
                    val delayString = (departure as JsonObject)["departure_delay"]?.jsonPrimitive?.content
//                    println(" delayString is $delayString seconds")
                    val delay = if (delayString != "null") {
                        delayString!!.toInt() / 60 // convert seconds to minutes
                    } else {
                        Duration.between(scheduledOut, actualOut).toMinutes().toInt()
                    }
//                    println("delay is $delay mins")
                    val hourIndex = (Duration.between(eightHoursAgo, actualOut).toHours() % 8).toInt()
                    flightsByHour[hourIndex]++
                    if (delay > 0) {
                        delaysByHour[hourIndex] += delay
                    }
                }else{
                    continue
                }
            }
        }
        println("printing results")
        for (i in 0..7) {
            val hourStart = eightHoursAgo.plusHours(i.toLong())
            val hourEnd = eightHoursAgo.plusHours((i + 1).toLong())
            val hourDelay = if (flightsByHour[i] > 0) delaysByHour[i] / flightsByHour[i] else 0
            println("${hourStart.format(DateTimeFormatter.ISO_LOCAL_TIME)}-${hourEnd.format(DateTimeFormatter.ISO_LOCAL_TIME)}: $hourDelay minutes")
        }


        client.close()
        call.respond(200)
    }
}