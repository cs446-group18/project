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
import com.cs446group18.delaywise.ui.styles.BodyText
import com.cs446group18.delaywise.ui.styles.Heading
import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.FlightStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun FullRow(content: @Composable() () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround) {
        content()
    }
}

@Composable
fun FullCard(content: @Composable() () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(15.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            content()
        }
    }
}

val dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

@Composable
fun BasicInfoCard(
    label: String,
    airportIata: String?,
    airportName: String?,
    estimatedTime: LocalDateTime?,
    scheduledTime: LocalDateTime?,
    terminal: String?,
    gate: String?,
) {
    FullCard {
        Heading(
            "$airportIata: $airportName"
        )
        Row (modifier = Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Start)
        {
            if (scheduledTime != null) {
                BodyText("Date: ${scheduledTime.format(dateFormatter)}")
            }
        }
        Divider(thickness = 2.dp, modifier = Modifier.padding(3.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BodyText("$label Time")
                if (estimatedTime != null && scheduledTime != null) {
                    var color = Color.Red // todo: make this dynamic
                    BodyText(scheduledTime!!.format(timeFormatter).toString(),
                        color = Color.LightGray,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                    )
                    BodyText(estimatedTime.format(timeFormatter).toString(), color = color)
                } else if (estimatedTime != null) {
                    BodyText(estimatedTime.format(timeFormatter).toString())
                } else if (scheduledTime != null) {
                    BodyText(scheduledTime.format(timeFormatter).toString())
                } else {
                    BodyText("Error: no time data", color = Color.Gray)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BodyText("Terminal")
                BodyText(terminal ?: "–")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BodyText("Gate")
                BodyText(gate ?: "–")
            }
        }
    }
}


// TODO: move to ui.flightinfo package because it's specific to that screen
@Composable
fun FlightInfoUI(flightInfoData: FlightInfo) {
    Column(modifier = Modifier
        .padding(vertical = 10.dp)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())) {
        FullRow {
            Heading(flightInfoData.airlineName ?: "Unknown Airline")
            if (flightInfoData.flightIata != null) {
                Heading(flightInfoData.flightIata.toString())
            }
        }
        FullRow {
            if (flightInfoData.delay != null) {
                BodyText(
                    "Delayed ${flightInfoData.delay} min",
                    color = when(flightInfoData.delay) {
                        in (Int.MIN_VALUE)..15 -> Color.Green
                        in 15..30 -> Color(0xFFFF9900) // Yellow/Orange
                        else -> Color.Red
                    }
                )
            } else {
                BodyText("On Time")
            }
            val flightDuration = flightInfoData.flightDuration
            if (flightDuration != null) {
                BodyText("Duration: ${flightDuration.toDuration(DurationUnit.MINUTES)}")
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        FullCard {
            Row {
                Heading("Predicted Delay by Amadeus")
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BodyText( flightInfoData.delay?.toString() ?: "15-30", color = Color(0xFFFFA500)) // Orange
                    BodyText("Projected Delay")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BodyText("45%")
                    BodyText("Likelihood")
                }
            }
        }
        BasicInfoCard(
            label = "Departure",
            airportIata = flightInfoData.depAirportIata,
            airportName = flightInfoData.depAirportName,
            estimatedTime = flightInfoData.depEstimated,
            scheduledTime = flightInfoData.depScheduled,
            terminal = flightInfoData.depTerminal,
            gate = flightInfoData.depGate
        )
        BasicInfoCard(
            label = "Arrival",
            airportIata = flightInfoData.arrAirportIata,
            airportName = flightInfoData.arrAirportName,
            estimatedTime = flightInfoData.arrEstimated,
            scheduledTime = flightInfoData.arrScheduled,
            terminal = flightInfoData.arrTerminal,
            gate = flightInfoData.arrGate
        )
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
    }
}

@Preview
@Composable
fun PreviewFlightInfoCard() = FlightInfoUI(
    flightInfoData = FlightInfo(
        flightIata = "AC8838",
        flightDuration = 107,
        flightStatus = FlightStatus.DELAYED,
        flightNumber = "8838",
        airlineIata = "AC",
        airlineName = "Air Canada",
        depAirportIata = "YYZ",
        depAirportName = "Toronto Pearson International Airport",
        depTerminal = "1",
        depGate = "F88",
        depCity = "Toronto",
        depCountry = "CA",
        arrAirportIata = "RDU",
        arrAirportName = "Raleigh-Durham International Airport",
        arrTerminal = "2",
        arrCity = "Raleigh/Durham",
        arrCountry = "US",
        delay = 35,
        depScheduled = LocalDateTime.now(),
        depEstimated = LocalDateTime.now().plusMinutes(5), // late 5 mins
        arrScheduled = LocalDateTime.now().plusHours(1),
        arrEstimated = LocalDateTime.now().plusHours(1).minusMinutes(5), // early 5 mins
        arrGate = null,
    )
)
