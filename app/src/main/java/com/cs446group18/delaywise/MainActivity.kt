package com.cs446group18.delaywise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cs446group18.delaywise.ui.NavGraphs
import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.FlightInfoResponse
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ClientModel.init(this)
        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}
