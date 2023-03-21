package com.cs446group18.delaywise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cs446group18.delaywise.ui.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val airlineCodes = assets.open("airline_codes.csv").bufferedReader().use { it.readText() }.trimIndent()
        val airportCodes = assets.open("airport_codes.csv").bufferedReader().use { it.readText() }.trimIndent()
        ClientModel.setAirlines(airlineCodes)
        ClientModel.setAirports(airportCodes)
        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}
