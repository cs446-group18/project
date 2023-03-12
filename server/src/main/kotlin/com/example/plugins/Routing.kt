package com.example.plugins

import com.example.routes.flightInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.util.Identity.decode

fun Application.configureRouting() {
    routing {
        flightInfo()
    }
}



