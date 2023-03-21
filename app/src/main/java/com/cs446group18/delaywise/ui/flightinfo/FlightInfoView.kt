package com.cs446group18.delaywise.ui.flightinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.getAirlineName
import com.cs446group18.delaywise.ui.components.*
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.components.FlightInfoUI
import com.cs446group18.delaywise.ui.components.TopBar
import com.cs446group18.delaywise.ui.styles.headingFont
import com.cs446group18.delaywise.util.UiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator


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
        when (val state = flightInfoViewModel.flightState.collectAsState(UiState.Loading()).value) {
            is UiState.Loading -> {
                LoadingCircle()
            }
            is UiState.Error -> {
                ErrorMessage(state.message)
            }
            is UiState.Loaded -> {
                val flightInfo = state.data
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
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            tint = Color(0xFF2096F3),
                            contentDescription = "Save Page Icon",
                            modifier = Modifier.size(30.dp).align(Alignment.CenterVertically).clickable(onClick = {})
                        )
                    }
                    FlightInfoUI(
                        flightInfoData = flightInfo
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewFlightInfoView() = FlightInfoView(EmptyDestinationsNavigator, "AC8838")
