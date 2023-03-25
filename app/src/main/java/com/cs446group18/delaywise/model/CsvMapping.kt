package com.cs446group18.delaywise.model

import android.content.Context
import com.cs446group18.lib.models.FlightInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.decodeFromString

@Serializable
data class Airport(
    override val iata: String,
    val icao: String,
    val airport: String,
) : IataMapping

@Serializable
data class Airline(
    override val iata: String,
    val icao: String,
    val airline: String,
) : IataMapping

fun FlightInfo.getAirlineName(): String? {
    return ClientModel.getInstance().airlinesByIata[operator_iata]?.airline
}

interface IataMapping {
    val iata: String
}

inline fun <reified T : IataMapping> loadMapping(
    context: Context,
    filename: String
): Map<String, T> {
    val rawText = context.assets.open(filename).bufferedReader().use { it.readText() }.trimIndent()
    return Csv { hasHeaderRecord = true }.decodeFromString<List<T>>(rawText).associateBy { it.iata }
}