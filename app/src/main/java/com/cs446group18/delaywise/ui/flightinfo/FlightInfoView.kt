package com.cs446group18.delaywise.ui.flightinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.cs446group18.delaywise.ui.components.BottomBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun FlightInfoView(
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        bottomBar = { BottomBar(navigator) },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text("FlightInfoView")
            Button(onClick = { scope.launch { } }) {
                Text("Go to Home")
            }
        }
    }
}
