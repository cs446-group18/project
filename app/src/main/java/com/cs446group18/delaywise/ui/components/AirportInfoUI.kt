package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.ui.styles.BodyText
import com.cs446group18.delaywise.ui.styles.Heading
import com.cs446group18.lib.models.Airport
import com.cs446group18.lib.models.Weather
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Composable
fun AirportInfoUI(
    airportInfoData: Airport,
    timeLabels: List<String>,
    airportDelay: List<Int>,
    weatherObservations: List<Weather>
) {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {

        FullRow {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Heading("City")
                BodyText(airportInfoData.city)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Heading("Time Zone")
                BodyText(airportInfoData.timezone)
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        FullCard {
            Row {
                Heading("Historical Delays Over:")
                DropdownMenu(expanded = false, onDismissRequest = { /*TODO*/ }) {
                    DropdownMenuItem(text = { Text("7 days") }, onClick = { /*TODO*/ })
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BodyText("43%")
                    BodyText("rate of delay")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BodyText("25 min")
                    BodyText("avg. delay")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BodyText("10%")
                    BodyText("cancellation rate")
                }
            }
        }
        FullCard {
            Row {
                Heading("Weather:")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    WeatherCard(
                        weather = weatherObservations[0],
                        navigator = EmptyDestinationsNavigator

                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    WeatherCard(
                        weather = weatherObservations[1],
                        navigator = EmptyDestinationsNavigator

                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    WeatherCard(
                        weather = weatherObservations[2],
                        navigator = EmptyDestinationsNavigator

                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        FullCard {
            LabeledCongestionGraph(timeLabels, airportDelay)
        }
    }
}