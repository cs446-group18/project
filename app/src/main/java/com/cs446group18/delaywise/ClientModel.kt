package com.cs446group18.delaywise

import android.os.Environment
import com.cs446group18.lib.models.Fetcher
import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.Model
import com.opencsv.CSVReader
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

val client = HttpClient(CIO) {
    install(Logging)
}

data class ClientFetcher(
    val apiKey: String? = null
) : Fetcher {
    override suspend fun makeAeroApiCall(url: String): HttpResponse {
        val baseUrl = when(apiKey) {
            null -> "http://10.0.2.2:8082"
            else -> "https://aeroapi.flightaware.com/aeroapi"
        }
        val response = client.get(baseUrl + url) {
            headers {
                if(apiKey != null) {
                    append("x-apikey", apiKey)
                }
            }
        }
        if(response.status != HttpStatusCode.OK) {
            throw Exception("returned invalid status code ${response.status}")
        }
        return response
    }
}

@Serializable
data class Airport(
    val iata: String,
    val icao: String,
    val airport: String,
)

@Serializable
data class Airline(
    val iata: String,
    val icao: String,
    val airline: String,
)

fun FlightInfo.getAirlineName(): String? {
    val airlinesByIata = ClientModel.getAirlines()
    return airlinesByIata[operator_iata]?.airline
}

object ClientModel : Model(ClientFetcher()) {
    private var airlinesByIata : Map<String, Airline>? = null
    private var airportsByIata : Map<String, Airport>? = null

    fun setAirlines(airlineCodes: String) {
        airlinesByIata = Csv { hasHeaderRecord = true }.decodeFromString<List<Airline>>(airlineCodes).associateBy{ it.iata }
    }
    fun setAirports(airportCodes: String) {
        airportsByIata = Csv { hasHeaderRecord = true }.decodeFromString<List<Airport>>(airportCodes).associateBy{ it.iata }
    }
    fun getAirlines() : Map<String, Airline> {
        val cached = this.airlinesByIata
        if(cached != null) return cached
        val airlineCsv = Files.readAllBytes(Paths.get(Environment.getExternalStorageDirectory().path + "/airline_codes.csv"))
        val airlinesByIata = Csv { hasHeaderRecord = true }.decodeFromString<List<Airline>>(airlineCsv.toString()).associateBy{ it.iata }
        this.airlinesByIata = airlinesByIata
        return airlinesByIata
    }
    fun getAirports() : Map<String, Airport> {
        val cached = this.airportsByIata
        if(cached != null) return cached
        val airportCsv = Files.readAllBytes(Paths.get(Environment.getExternalStorageDirectory().path + "./airport_codes.csv"))
        val airportsByIata = Csv { hasHeaderRecord = true }.decodeFromString<List<Airport>>(airportCsv.toString()).associateBy{ it.iata }
        this.airportsByIata = airportsByIata
        return airportsByIata
    }

    suspend fun getFlight(flightIata: String) : FlightInfo {
        val match = """^(.*?)(\d+)$""".toRegex().matchEntire(flightIata)
        match ?: throw Exception("could not extract airline code from $flightIata")
        val (airlineIata, flightNumber) = match.destructured
        val airlinesByIata = getAirlines()
        val airlineIcao = airlinesByIata[airlineIata]?.icao ?: throw Exception("could not find matching an airline matching $airlineIata")
        val flightInfoResponse = getFlightRaw(airlineIcao + flightNumber)
        val activeInstance = flightInfoResponse.flights.find {
            it.scheduled_out.toLocalDateTime(TimeZone.currentSystemDefault()).date ==
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        } ?: flightInfoResponse.flights.minBy {
            (it.scheduled_out - Clock.System.now()).absoluteValue
        }
        return activeInstance
    }
}

