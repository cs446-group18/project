package com.cs446group18.delaywise

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.ui.NavGraphs
import com.cs446group18.delaywise.ui.destinations.FlightInfoViewDestination
import com.cs446group18.delaywise.ui.destinations.HomeViewDestination
import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.FlightInfoResponse
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.spec.Route
import com.ramcosta.composedestinations.utils.startDestination
import kotlinx.datetime.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ClientModel.init(this)
        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
        val intent = Intent(this, NotificationService::class.java)
        startService(intent)
    }
}
