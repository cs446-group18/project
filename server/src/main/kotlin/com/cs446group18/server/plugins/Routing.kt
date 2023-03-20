package com.cs446group18.server.plugins

import com.cs446group18.server.routes.flightInfo
import com.cs446group18.server.routes.airportInfo
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        flightInfo()
        airportInfo()
        get("/") {
            call.respond(200)
        }
    }
}
