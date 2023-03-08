package com.cs446group18.delaywise.ui.savedflights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.components.DelayType
import com.cs446group18.delaywise.ui.components.SavedFlightCard
import com.cs446group18.delaywise.ui.components.TopBar
import com.cs446group18.delaywise.ui.home.HomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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
            SavedFlightCard(flightData = HomeViewModel.Flight("AC8839", "Delayed", DelayType.DELAYED, "RDU(Raleigh)", "YYZ(Toronto)", "Delayed to 5pm; Wed 15 March, 2023"))
            SavedFlightCard(flightData = HomeViewModel.Flight("AC834", "Likely 15m Delay", DelayType.LIKELY, "YYZ(Toronto)", "MUC(Munich)", "Scheduled 8:30pm; Wed 15 March, 2023"))
            SavedFlightCard(flightData = HomeViewModel.Flight("LH1810", "On-Time", DelayType.ONTIME, "MUC(Munich)", "BCN(Barcelona)", "Scheduled 9am; Mon 16 March, 2023"))
            SavedFlightCard(flightData = HomeViewModel.Flight("AC8838", "Likely 1h Delay", DelayType.LIKELY, "YYZ(Toronto)", "RDU(Raleigh)", "Scheduled 2pm; Mon 21 March, 2023"))
        }
    }
}
