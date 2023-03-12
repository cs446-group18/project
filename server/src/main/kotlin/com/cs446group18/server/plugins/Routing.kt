package com.cs446group18.server.plugins

import com.cs446group18.server.routes.flightInfo
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        flightInfo()
    }
}
