package com.cs446group18.delaywise.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.ui.components.*
import com.cs446group18.delaywise.ui.flightinfo.SavedFlightsViewModel
import com.cs446group18.delaywise.ui.styles.headingFont
import com.cs446group18.delaywise.ui.styles.bodyFont
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.FlightInfo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


//todo: delete old list
//private val savedFlightsList = mutableListOf(
//    HomeViewModel.Flight("AC8836", "AC", "On Time", DelayType.ONTIME, "YYZ(Toronto)", "RDU(Raleigh)", "Scheduled 2:05pm; Mon 13 March, 2023"),
//    HomeViewModel.Flight("AC8839", "AC", "Delayed", DelayType.DELAYED, "RDU(Raleigh)", "YYZ(Toronto)", "Delayed to 5pm; Wed 15 March, 2023"),
//    HomeViewModel.Flight("AC834", "AC","Likely 15m Delay", DelayType.LIKELY, "YYZ(Toronto)", "MUC(Munich)", "Scheduled 8:30pm; Wed 15 March, 2023"),
//    HomeViewModel.Flight("LH1810", "LH","Likely 17m Delay", DelayType.LIKELY, "MUC(Munich)", "BCN(Barcelona)", "Scheduled 9am; Mon 16 March, 2023"),
//    HomeViewModel.Flight("AC8838", "AC","Likely 1h Delay", DelayType.LIKELY, "YYZ(Toronto)", "RDU(Raleigh)", "Scheduled 2pm; Mon 21 March, 2023"),
//)
@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeView(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = viewModel { HomeViewModel()}
) {
    val focusManager = LocalFocusManager.current
    val state by homeViewModel.homeSavedFlightState.collectAsState()

    Scaffold(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { focusManager.clearFocus() }
        ),
        topBar = {
            TopBar(navigator)
        },
        bottomBar = {
            BottomBar(navigator)
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.homepage_bg),
                    contentScale = ContentScale.FillHeight,
                )
                .padding(top = 70.dp)
                .padding(horizontal = 30.dp)
                .padding(contentPadding.calculateEndPadding(layoutDirection = LayoutDirection.Ltr))

        ) {
            Text("Welcome to", fontFamily = headingFont, fontSize = 28.sp)
            Text("DelayWise!", fontFamily = headingFont, fontSize = 40.sp)
            Text("Enter a flight number or airport:", fontFamily = bodyFont, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(15.dp))
            SearchBox(navigator)
            Spacer(modifier = Modifier.height(15.dp))
            Text("Saved Flights/Airports", fontSize = 28.sp, fontFamily = headingFont)
            when (state) {
                is UiState.Loading -> {
                    LoadingCircle()
                }
                is UiState.Error -> {
                    val message = (state as UiState.Error).message
                    ErrorMessage(message)
                }
                is UiState.Loaded -> {
                    val savedFlights = (state as UiState.Loaded<List<SavedFlightEntity>>).data
                    if (savedFlights.isEmpty()) {
                        Box (
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()){
                            Text("Nothing saved yet!",
                                modifier = Modifier.background(Color(0x99F6F2FA), RectangleShape),
                                fontFamily = bodyFont,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center)
                        }
                    }
                    else{
                        for (savedFlight in savedFlights) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ){
                                var flightInfo = Json.decodeFromString<FlightInfo>(savedFlight.json);
                                SavedFlightCard(flightInfo, navigator)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() = HomeView(EmptyDestinationsNavigator)
