package com.cs446group18.delaywise

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.ui.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost

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
