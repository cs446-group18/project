package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.model.getAirlineName
import com.cs446group18.delaywise.ui.airportinfo.timeLabelGenerator
import com.cs446group18.delaywise.ui.flightinfo.FlightInfoViewModel
import com.cs446group18.delaywise.ui.styles.BodyText
import com.cs446group18.delaywise.ui.styles.Heading
import com.cs446group18.delaywise.util.formatAsDate
import com.cs446group18.delaywise.util.formatAsTime
import com.cs446group18.delaywise.util.formatInHoursMinutes
import com.cs446group18.lib.models.Airport
import com.cs446group18.lib.models.FlightInfo
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.datetime.*
import java.util.*
import kotlin.time.Duration

@Composable
fun AirportInfoUI(airportInfoData: Airport) {
    Column(modifier = Modifier
        .padding(vertical = 10.dp)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())) {

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
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround) {
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

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Heading("Weather")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    WeatherCard(
                        weatherData = FlightInfoViewModel.WeatherData(
                            "Wednesday",
                            "Sunny"
                        )
                        , navigator = EmptyDestinationsNavigator

                    )
                }
            }
        }


    }
}