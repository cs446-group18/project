package com.cs446group18.delaywise

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.ui.NavGraphs
import com.cs446group18.delaywise.ui.destinations.FlightInfoViewDestination
import com.cs446group18.delaywise.ui.destinations.HomeViewDestination
import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.FlightInfoResponse
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.Route

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ClientModel.init(this)
        val destination = when(intent?.action) {
            "FlightInfoView" -> FlightInfoViewDestination(intent.getStringExtra("flightIata")!!)
            else -> HomeViewDestination()
        } as Route
        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root, startRoute = destination)
        }
    }

    override fun startService(service: Intent?): ComponentName? {
        val intent = Intent(this, NotificationService::class.java)
        return startService(intent)
    }
}
