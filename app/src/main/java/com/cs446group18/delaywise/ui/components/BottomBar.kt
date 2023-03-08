package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.ui.destinations.SavedFlightsViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Composable
fun BottomBar(
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()
    Surface(
        shape = RoundedCornerShape(100),
        color = Color.LightGray, // todo: use figma color
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            IconButton(onClick = {
                scope.launch { navigator.navigate(SavedFlightsViewDestination) }
            }) {
                Icon(
                    Icons.Filled.FlightTakeoff,
                    contentDescription = "Saved Flights"
                )
            }
            FloatingActionButton(
                onClick = { /* do something */ },
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Flight",
                    modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize),
                )
            }
            IconButton(onClick = {
                scope.launch { /*navigator.navigate(SavedFlightsViewDestination)*/ }
            }) {
                Icon(
                    Icons.Filled.Tune,
                    contentDescription = "Settings"
                )
            }
        }
    }
}
