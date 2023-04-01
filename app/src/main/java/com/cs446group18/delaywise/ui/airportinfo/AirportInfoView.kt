package com.cs446group18.delaywise.ui.airportinfo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.model.getAirlineName
import com.cs446group18.delaywise.ui.components.*
import com.cs446group18.delaywise.ui.styles.headingFont
import com.cs446group18.delaywise.util.UiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AirportInfoView(
    navigator: DestinationsNavigator,
    airportCode: String,
    airportInfoViewModel: AirportInfoViewModel = viewModel { AirportInfoViewModel(airportCode) }
) {
    Scaffold(
        topBar = {
            TopBar(navigator)
        },
        bottomBar = {
            BottomBar(navigator)
        },
    ) { contentPadding ->
        val airportState by airportInfoViewModel.airportState.collectAsState()
        when (airportState)
        {
            is UiState.Loading -> {
                LoadingCircle()
            }
            is UiState.Error -> {
                val message = (airportState as UiState.Error).message
                ErrorMessage(message)
            }
            is UiState.Loaded -> {
                val airport = (airportState as UiState.Loaded).data.first
                val airportDelay = (airportState as UiState.Loaded).data.second
                Column(
                    modifier = Modifier.padding(contentPadding) ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp,Alignment.CenterHorizontally)) {
                        Text(
                            (airport.cleanName() + " (${airport.code_iata})") ?: "Unknown Airport",
                            fontFamily = headingFont,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.absolutePadding(left = 10.dp)
                        )
                    }
                    Row() {
                        DisplaySaveToggleButton(
                            airport.code_iata,
                            passedModifier = Modifier
                                .align(Alignment.CenterVertically),
                            airportInfoViewModel.isSaved,
                            airportInfoViewModel::saveActionTriggered,
                            airportInfoViewModel::removeActionTriggered
                        )
                    }
                    AirportInfoUI(airportInfoData = airport)
                    println(airport)
                    println(airportDelay)
                    //todo: @hanz- figure out why graph is reaching a range exception when you search for airport YYZ
//                    LabeledCongestionGraph(navigator, timeLabelGenerator(airportDelay.size), airportDelay)
                }
            }
        }
    }
}

fun timeLabelGenerator(numPoints : Int): List<String> {
    val rightNow = Calendar.getInstance()
    val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
    val times = mutableListOf<String>()
    for (i in numPoints - 1 downTo 0 step 1) {
        val curr = currentHourIn24Format - i
        times.add((if (curr % 12 === 0) "12" else (curr % 12)).toString() + if (curr >= 12) "PM" else "AM")
    }
    return times
}

@Preview
@Composable
fun Preview() = AirportInfoView(EmptyDestinationsNavigator, "YYZ")