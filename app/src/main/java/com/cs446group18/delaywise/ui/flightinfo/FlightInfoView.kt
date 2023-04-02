package com.cs446group18.delaywise.ui.flightinfo

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.model.calcHistorical
import com.cs446group18.delaywise.model.filterPickableFlights
import com.cs446group18.delaywise.model.getAirlineName
import com.cs446group18.delaywise.ui.components.*
import com.cs446group18.delaywise.ui.styles.bodyFont
import com.cs446group18.delaywise.ui.styles.headingFont
import com.cs446group18.delaywise.util.UiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun FlightInfoView(
    navigator: DestinationsNavigator,
    flightIata: String,
    date: LocalDate? = null,
    flightInfoViewModel: FlightInfoViewModel = viewModel { FlightInfoViewModel(flightIata, date) },
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
                val (flightInfo, flightList) = (state as UiState.Loaded).data
                Column(
                    modifier = Modifier.padding(contentPadding) ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp,Alignment.CenterHorizontally)) {
                        Text(
                            "${flightInfo.getAirlineName() ?: flightInfo.operator_iata} ${flightInfo.flight_number}",
                            fontFamily = headingFont,
                            fontSize = 32.sp,
                            modifier = Modifier.absolutePadding(left = 10.dp)
                        )
                        DisplaySaveToggleButton(
                            flightInfo.ident_iata,
                            passedModifier = Modifier
                                .align(Alignment.CenterVertically),
                            flightInfoViewModel.isSaved,
                            flightInfoViewModel::saveActionTriggered,
                            flightInfoViewModel::removeActionTriggered
                        )
                    }
                    Text("Choose a Departure Date: ",
                        fontFamily = bodyFont,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Row(modifier = Modifier.padding(10.dp)) {
                        DateDropdown(
                            suggestions = flightList.filterPickableFlights().map{ it.getDepartureDate() },
                            defaultDate = flightInfo.scheduled_out,
                            changeDateFunc = flightInfoViewModel::dateChangeTriggered,
                            isReadOnly = true,
                        )
                    }
                    val historical = flightList.calcHistorical()
                    FlightInfoUI(
                        flightInfoData = flightInfo,
                        navigator = navigator,
                        historical = historical
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewFlightInfoView() = FlightInfoView(EmptyDestinationsNavigator, "AC8838")
