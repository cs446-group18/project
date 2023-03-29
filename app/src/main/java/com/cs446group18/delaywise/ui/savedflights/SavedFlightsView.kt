package com.cs446group18.delaywise.ui.savedflights

import android.media.Image
import androidx.compose.foundation.Image
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
import com.cs446group18.delaywise.ui.components.TopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.ui.components.*
import com.cs446group18.delaywise.ui.flightinfo.FlightInfoViewModel
import com.cs446group18.delaywise.ui.flightinfo.SavedFlightsViewModel
import com.cs446group18.delaywise.ui.home.HomeViewModel
import com.cs446group18.delaywise.ui.styles.bodyFont
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.FlightInfo
import com.patrykandpatrick.vico.core.component.dimension.Padding
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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
    navigator: DestinationsNavigator,
    savedFlightsViewModel: SavedFlightsViewModel = viewModel { SavedFlightsViewModel()},
) {
    Scaffold(
        topBar = {
            TopBar(navigator)
        },
        bottomBar = {
            BottomBar(navigator)
        },
    ) { contentPadding ->
            when (val state = savedFlightsViewModel.savedFlightState.collectAsState(UiState.Loading()).value) {
                is UiState.Loading -> {
                    LoadingCircle()
                }
                is UiState.Error -> {
                    ErrorMessage(state.message)
                }
                is UiState.Loaded -> {
                    val savedFlights = state.data
                    Column(modifier = Modifier.padding(contentPadding).verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            "Saved Flights",
                            fontFamily = appFontFamily,
                            fontSize = 40.sp,
                            modifier = Modifier.absolutePadding(left = 20.dp)
                        )
                        if (savedFlights.isEmpty()) {
                            Column(
                                modifier = Modifier.padding(vertical = 150.dp, horizontal = 50.dp).fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Image(
                                    modifier = Modifier.fillMaxWidth(),
                                    painter = painterResource(R.drawable.no_flight),
                                    contentDescription = null
                                )
                                Text("No saved flights... yet!",
                                    fontFamily = bodyFont,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center)
                            }
                        }
                        else{
                            for (savedFlight in savedFlights) {
                                var flightInfo = Json.decodeFromString<FlightInfo>(savedFlight.json);
                                SavedFlightCard(flightInfo, navigator)
                            }
                        }
                    }
                }
            }
        }
    }

/* //todo: Delete Mocked SavedFlightCards
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
 */