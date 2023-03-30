package com.cs446group18.delaywise.ui.airportinfo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        when (val state =
            airportInfoViewModel.airportDelay.collectAsState(UiState.Loading()).value) {
            is UiState.Loading -> {
                LoadingCircle()
            }
            is UiState.Error -> {
                ErrorMessage(state.message)
            }
            is UiState.Loaded -> {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)) {
                    Text(
                        airportCode, /* todo fix so it's airport data: "${flightInfo.getAirlineName() ?: flightInfo.operator_iata} ${flightInfo.flight_number}"*/
                        fontFamily = headingFont,
                        fontSize = 32.sp,
                        modifier = Modifier.absolutePadding(left = 10.dp)
                    )
//                    DisplaySaveToggleButton(
//                        airportCode,
//                        passedModifier = Modifier
//                            .align(Alignment.CenterVertically),
//                        airportInfoViewModel.isSaved,
//                        airportInfoViewModel::saveActionTriggered,
//                        airportInfoViewModel::removeActionTriggered
//                    )
                }
                val airportDelay = state.data
                Column(modifier = Modifier.padding(contentPadding)) {
                    LabeledCongestionGraph(navigator,
                        fakeApi(airportDelay.size), airportDelay)
                }
            }
        }
    }
}

fun fakeApi(numPoints : Int): List<String> {
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