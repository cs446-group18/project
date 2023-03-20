package com.cs446group18.server.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.airportInfo() {
    get("/airport") {
        call.respond(200)
    }
}