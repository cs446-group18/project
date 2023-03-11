package com.cs446group18.delaywise.ui.savedflights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.components.DelayType
import com.cs446group18.delaywise.ui.components.SavedFlightCard
import com.cs446group18.delaywise.ui.components.TopBar
import com.cs446group18.delaywise.ui.home.HomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.cs446group18.delaywise.ui.components.*
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
fun SavedFlightsView(
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            TopBar(navigator)
        },
        bottomBar = {
            BottomBar(navigator)
        },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text(
                "Saved Flights",
                fontFamily = appFontFamily,
                fontSize = 40.sp,
                modifier = Modifier.absolutePadding(left = 10.dp)
            )
            SavedFlightCard(
                flightData = HomeViewModel.Flight(
                    "AC8839",
                    "AC",
                    "Delayed",
                    DelayType.DELAYED,
                    "RDU(Raleigh)",
                    "YYZ(Toronto)",
                    "Delayed to 5pm; Wed 15 March, 2023"
                ),
                navigator = navigator
            )
            SavedFlightCard(
                flightData = HomeViewModel.Flight(
                    "AC834",
                    "AC",
                    "Likely 15m Delay",
                    DelayType.LIKELY,
                    "YYZ(Toronto)",
                    "MUC(Munich)",
                    "Scheduled 8:30pm; Wed 15 March, 2023"
                ),
                navigator = navigator
            )
            SavedFlightCard(
                flightData = HomeViewModel.Flight(
                    "LH1810",
                    "LH",
                    "On-Time",
                    DelayType.ONTIME,
                    "MUC(Munich)",
                    "BCN(Barcelona)",
                    "Scheduled 9am; Mon 16 March, 2023"
                ),
                navigator = navigator
            )
            SavedFlightCard(
                flightData = HomeViewModel.Flight(
                    "AC8838",
                    "AC",
                    "Likely 1h Delay",
                    DelayType.LIKELY,
                    "YYZ(Toronto)",
                    "RDU(Raleigh)",
                    "Scheduled 2pm; Mon 21 March, 2023"
                ),
                navigator = navigator
            )
        }
    }
}

@Preview
@Composable
fun PreviewSavedFlightView() = SavedFlightsView(EmptyDestinationsNavigator)
