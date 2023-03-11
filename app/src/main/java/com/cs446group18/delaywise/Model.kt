package com.cs446group18.delaywise

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*

val client = HttpClient(CIO) {
    install(Logging)
}

object Model {
    suspend fun getFlight() {
        val response = client.get("http://10.0.2.2:8082/flightInfo/AC741")
        println(response.bodyAsText())
    }
}
