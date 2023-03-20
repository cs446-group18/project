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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cs446group18.delaywise.ui.destinations.AirportInfoViewDestination
import com.cs446group18.delaywise.ui.destinations.SavedFlightsViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

@Composable
fun BottomBar(
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()
    ConstraintLayout {
        val floatingActionButton = createRef()
        Surface(
            shadowElevation = 10.dp,
//            tonalElevation = 20.dp,
            shape = RoundedCornerShape(100),
            color = Color(0xCCF6F7FA),
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp, 30.dp)
                .height(40.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(45.dp, 0.dp)
            ) {

                IconButton(onClick = {
                    scope.launch { navigator.navigate(SavedFlightsViewDestination) }
                }) {
                    Icon(
                        Icons.Filled.FlightTakeoff,
                        tint = Color(0x99000000),
                        contentDescription = "Saved Flights"
                    )
                }
                IconButton(onClick = {
                    scope.launch { navigator.navigate(AirportInfoViewDestination) }
                }) {
                    Icon(
                        Icons.Filled.Tune,
                        tint = Color(0x99000000),
                        contentDescription = "Settings"
                    )
                }
            }
        }
        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(10.dp),
            onClick = { /* do something */ },
            containerColor = Color(0xFF007AFF),
            modifier = Modifier
                .scale(1.2f)
                .clip(RoundedCornerShape(100))
                .constrainAs(floatingActionButton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
        ) {
            Icon(
                Icons.Filled.Add,
                tint = Color.White,
                contentDescription = "Add Flight",
                modifier = Modifier.scale(1.4f),
            )
        }
    }
}

@Preview
@Composable
fun PreviewBottomBar() = BottomBar(EmptyDestinationsNavigator)

