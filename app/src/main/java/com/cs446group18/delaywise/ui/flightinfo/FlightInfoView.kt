package com.cs446group18.delaywise.ui.flightinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.components.FlightInfoCard
import com.cs446group18.delaywise.ui.components.TopBar
import com.cs446group18.delaywise.util.UiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

private val appFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.montserrat_extrabold,
            weight = FontWeight.W900,
            style = FontStyle.Normal
        ),
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun FlightInfoView(
    navigator: DestinationsNavigator,
    flightInfoViewModel: FlightInfoViewModel = viewModel(),
) {
    Scaffold(
        topBar = {
            TopBar(navigator)
        },
        bottomBar = {
            BottomBar(navigator)
        },
    ) { contentPadding ->
        when (val state = flightInfoViewModel.flightState.collectAsState(UiState.Empty()).value) {
            is UiState.Empty -> Text("empty")
            is UiState.Loading -> Text("loading")
            is UiState.Error -> Text("error: " + state.message)
            is UiState.Loaded -> {
                val flightInfo = state.data
                Column(
                    modifier = Modifier.padding(contentPadding) ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {Text(
                    "Lufthansa 256",
                    fontFamily = appFontFamily,
                    fontSize = 32.sp,
                    modifier = Modifier.absolutePadding(left = 10.dp)
                )
                    FlightInfoCard(
                        flightInfoData = flightInfo
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewFlightInfoView() = FlightInfoView(EmptyDestinationsNavigator)
