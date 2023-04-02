package com.cs446group18.delaywise.ui.components

import android.graphics.fonts.Font
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.model.getAirlineName
import com.cs446group18.delaywise.ui.destinations.AirportInfoViewDestination
import com.cs446group18.delaywise.ui.styles.*
import com.cs446group18.delaywise.util.*
import com.cs446group18.lib.models.Airport
import com.cs446group18.lib.models.FlightInfo
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.datetime.*
import kotlin.time.Duration

@Composable
fun FullRow(content: @Composable() () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround) {
        content()
    }
}

@Composable
fun FullColumn(content: @Composable() () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.padding(start = 16.dp, top = 10.dp)) {
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

@Composable
fun BasicInfoCard(
    label: String,
    airport: Airport,
    estimatedTime: Instant,
    scheduledTime: Instant,
    terminal: String,
    gate: String?,
) {
    FullCard {
        Heading(
            "${airport.code_iata}: ${airport.cleanName()}"
        )
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), horizontalArrangement = Arrangement.Start)
        {
            BodyText("Date: ${scheduledTime.formatAsDate()}")
        }
        Divider(thickness = 2.dp, modifier = Modifier.padding(3.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BodyText("$label Time")
                if(estimatedTime != scheduledTime) {
                    var color = Color.Red // todo: make this dynamic
                    BodyText(scheduledTime.formatAsTime(),
                        color = Color.LightGray,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                    )
                    BodyText(estimatedTime.formatAsTime(), color = color)
                } else {
                    BodyText(estimatedTime.formatAsTime())
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
fun FlightInfoUI(flightInfoData: FlightInfo, navigator: DestinationsNavigator, pastFlightInfoData: List<FlightInfo>) {
    Column(modifier = Modifier
        .padding(vertical = 10.dp)
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())) {

        FullRow {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Heading(flightInfoData.getAirlineName() ?: "Unknown Airline")
                BodyText(
                    when(flightInfoData.getDepartureDelay()) {
                        Duration.ZERO -> "On Time"
                        else -> "Delayed ${flightInfoData.getDepartureDelay().formatInHoursMinutes()}"
                    },
                    color = when(flightInfoData.getDepartureDelay().inWholeMinutes) {
                        in (Int.MIN_VALUE)..15 -> Color.Green
                        in 15..30 -> Color(0xFFFF9900) // Yellow/Orange
                        else -> Color.Red
                    }
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Heading(flightInfoData.ident_iata)
                BodyText("Duration: ${flightInfoData.getDuration().formatInHoursMinutes()}")
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
//        FullCard {
//            Row {
//                Heading("Predicted Delay by Amadeus")
//            }
//            Row(modifier = Modifier
//                .fillMaxWidth()
//                .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    // TODO: get real data from amadeus or historical data
//                    BodyText( "15-30 min", color = Color(0xFFFFA500)) // Orange
//                    BodyText("Projected Delay")
//                }
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    BodyText("45%")
//                    BodyText("Likelihood")
//                }
//            }
//        }
        FullColumn{
            LargeHeading("Flight Info")
        }
        BasicInfoCard(
            label = "Departure",
            airport = flightInfoData.origin,
            estimatedTime = flightInfoData.estimated_out ?: flightInfoData.scheduled_out,
            scheduledTime = flightInfoData.actual_out ?: flightInfoData.scheduled_out,
            terminal = flightInfoData.terminal_origin ?: "unknown terminal",
            gate = flightInfoData.gate_origin,
        )
        BasicInfoCard(
            label = "Arrival",
            airport = flightInfoData.destination,
            estimatedTime = flightInfoData.estimated_in ?: flightInfoData.scheduled_in,
            scheduledTime = flightInfoData.actual_in ?: flightInfoData.scheduled_in,
            terminal = flightInfoData.terminal_destination ?: "unknown terminal", // TODO: fix
            gate = flightInfoData.gate_destination,
        )
        FullColumn{
            LargeHeading("Historical Delays for Flight: " + flightInfoData.ident_iata)
        }
        if (pastFlightInfoData.isEmpty()) {
            FullRow {
                BodyText("Sorry, no data is available for this flight!")
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
        else {
            Spacer(modifier = Modifier.padding(10.dp))
            val delayStats = pastFlightInfoData.getDelayPrediction()
            println(pastFlightInfoData)
            println(delayStats)
            FullCard {
                Row {
                    Heading("In the last ${pastFlightInfoData.size} days, " + flightInfoData.ident_iata + " saw a:")
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        BodyText(delayStats[0])
                        BodyText("rate of delay")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        BodyText(delayStats[1])
                        BodyText("avg. delay")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        BodyText(delayStats[2])
                        BodyText("cancellation rate")
                    }
                }
            }
            val flightGraphInputs = pastFlightInfoData.getGraphInputs()
            FullCard {
                LabeledFlightDelayGraph(
                    flightGraphInputs.first,
                    flightGraphInputs.second
                )
            }
        }
        Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Heading("Check Departure Airport for Weather & Congestion Delays:", textAlign = TextAlign.Center)
            ClickableText(
                text = AnnotatedString("${flightInfoData.origin.code_iata}: ${flightInfoData.origin.cleanName()}"),
                style = TextStyle(color = Color.Blue, fontFamily = bodyFont, textDecoration = TextDecoration.Underline, fontSize = 18.sp ),
                onClick = {navigator.navigate(AirportInfoViewDestination(flightInfoData.origin.code_iata))}
            )
        }
    }
}

@Preview
@Composable
fun PreviewFlightInfoCard() = FlightInfoUI(
    flightInfoData = FlightInfo(
        ident_iata = "AC741",
        operator_iata = "AC",
        flight_number = "741",
        origin = Airport(
            code_iata = "YYZ",
            timezone = "America/Toronto",
            name = "Toronto Pearson Int'l",
            city = "Toronto",
        ),
        destination = Airport(
            code_iata = "SFO",
            timezone = "America/Los_Angeles",
            name = "San Francisco Int'l",
            city = "San Francisco",
        ),
        cancelled = false,
        departure_delay_raw = 20,
        arrival_delay_raw = 3,
        scheduled_out = Instant.parse("2023-03-18T20:30:00Z")!!,
        estimated_out = Instant.parse("2023-03-18T20:30:00Z")!!,
        actual_out = null,
        scheduled_in = Instant.parse("2023-03-19T02:25:00Z")!!,
        estimated_in = Instant.parse("2023-03-19T02:25:00Z")!!,
        actual_in = null,
        status = "Scheduled",
        gate_origin = "F53",
        gate_destination = null,
        terminal_origin = "1",
        terminal_destination = "2",
    ),
    navigator = EmptyDestinationsNavigator,
    pastFlightInfoData = emptyList()
)
