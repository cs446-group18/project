package com.cs446group18.delaywise.ui.flightinfo

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.model.calcHistorical
import com.cs446group18.delaywise.model.filterPickableFlights
import com.cs446group18.delaywise.model.getAirlineName
import com.cs446group18.delaywise.ui.components.*
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.components.FlightInfoUI
import com.cs446group18.delaywise.ui.components.TopBar
import com.cs446group18.delaywise.ui.styles.bodyFont
import com.cs446group18.delaywise.ui.styles.headingFont
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.HistoricalInfo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.datetime.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun FlightInfoView(
    navigator: DestinationsNavigator,
    flightIata: String,
    flightInfoViewModel: FlightInfoViewModel = viewModel { FlightInfoViewModel(flightIata) },
) {
    Scaffold(
        topBar = {
            TopBar(navigator)
        },
        bottomBar = {
            BottomBar(navigator)
        },
    ) { contentPadding ->
        val state by flightInfoViewModel.flightState.collectAsState()
        when (state) {
            is UiState.Loading -> {
                LoadingCircle()
            }
            is UiState.Error -> {
                val message = (state as UiState.Error).message
                ErrorMessage(message)
            }
            is UiState.Loaded -> {
                val flightInfo = (state as UiState.Loaded).data
                Column(
                    modifier = Modifier.padding(contentPadding) ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var dates = flightInfo.second.filterPickableFlights().map{it.scheduled_out}

                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp,Alignment.CenterHorizontally)) {
                        Text(
                            "${flightInfo.first.getAirlineName() ?: flightInfo.first.operator_iata} ${flightInfo.first.flight_number}",
                            fontFamily = headingFont,
                            fontSize = 32.sp,
                            modifier = Modifier.absolutePadding(left = 10.dp)
                        )
                        DisplaySaveToggleButton(
                            flightInfo.first.ident_iata,
                            passedModifier = Modifier
                                .align(Alignment.CenterVertically),
                            flightInfoViewModel.isSaved,
                            flightInfoViewModel::saveActionTriggered,
                            flightInfoViewModel::removeActionTriggered
                        )
                    }
                    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(10.dp)) {
                        Text("Choose a Departure Date: ",
                            fontFamily = bodyFont,
                            fontSize = 18.sp)
                        DateDropdown(suggestions = dates, defaultDate = flightInfo.first.scheduled_out, flightInfoViewModel::dateChangeTriggered, isReadOnly = true, timeZone = TimeZone.of(flightInfo.first.origin.timezone))
                    }
                    var historical = flightInfo.second.calcHistorical()
                    FlightInfoUI(
                        flightInfoData = flightInfo.first,
                        navigator = navigator,
                        historical = historical
                    )
                }
            }
        }
    }
}

//@Composable
//fun DateSelector(dates : List<String>) {
//    val contextForToast = LocalContext.current.applicationContext
//
//    // state of the menu
//    var expanded by remember {
//        mutableStateOf(false)
//    }
//
//    Box(
//        contentAlignment = Alignment.Center
//    ) {
//        // clock icon
//        IconButton(onClick = {
//            expanded = true
//        }) {
//            Icon(
//                imageVector = Icons.Default.Schedule,
//                contentDescription = "Open Options"
//            )
//        }
//
//        // drop down menu
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = {
//                expanded = false
//            }
//        ) {
//            // adding items
//            dates.forEach {itemValue ->
//                DropdownMenuItem(
//                    onClick = {
//                        Toast.makeText(contextForToast, itemValue, Toast.LENGTH_SHORT)
//                            .show()
//                        expanded = false
//                    },
//                    text = { Text(text = itemValue) }
//                )
//            }
//        }
//    }
//}

@Preview
@Composable
fun PreviewFlightInfoView() = FlightInfoView(EmptyDestinationsNavigator, "AC8838")
