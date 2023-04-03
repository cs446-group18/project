package com.cs446group18.delaywise.ui.home

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.model.Airline
import com.cs446group18.delaywise.model.SavedAirportEntity
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.ui.components.*
import com.cs446group18.delaywise.ui.destinations.FlightInfoViewDestination
import com.cs446group18.delaywise.ui.styles.BodyText
import com.cs446group18.delaywise.ui.styles.bodyFont
import com.cs446group18.delaywise.ui.styles.bodyStyle
import com.cs446group18.delaywise.ui.styles.headingFont
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.Airport
import com.cs446group18.lib.models.FlightInfo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeView(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    handleIntents(LocalContext.current as Activity, navigator)

    val focusManager = LocalFocusManager.current
    val state by homeViewModel.homeSavedState.collectAsState()
    val searchOptions = listOf("Flight", "Airport")
    val selectedText = remember { mutableStateOf(searchOptions[0]) }
    val airlinePair: MutableState<Pair<Airline?, TextFieldValue>> =
        remember { mutableStateOf(Pair(null, TextFieldValue(""))) }
    var flightNumber by remember { mutableStateOf("") }

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
                    contentScale = ContentScale.Crop,
                )
                .padding(top = 50.dp)
                .padding(horizontal = 30.dp)
                .padding(contentPadding.calculateEndPadding(layoutDirection = LayoutDirection.Ltr)),
            verticalArrangement = Arrangement.spacedBy(3.dp)

        ) {
            Text("Welcome to", fontFamily = headingFont, fontSize = 26.sp)
            Text("DelayWise!", fontFamily = headingFont, fontSize = 40.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BodyText("Search for:", fontSize = 18.sp)
                DropdownSmall(
                    suggestions = searchOptions,
                    mutableState = selectedText,
                    isReadOnly = true,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (selectedText.value == "Flight") { //User selected Search by Flight
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AirlineSearchBox(
                        homeViewModel.airlineResults.collectAsState().value,
                        mutableState = airlinePair
                    )
                    Spacer(Modifier.width(8.dp))
                    TextField(
                        modifier = Modifier.weight(0.35f, true),
                        singleLine = true,
                        textStyle = bodyStyle,
                        shape = RoundedCornerShape(8.dp),
                        placeholder = { BodyText("Flight #") },
                        value = flightNumber,
                        onValueChange = { flightNumber = it },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color(
                                R.color.main_blue
                            ).copy(
                                alpha = 1F
                            )
                        )
                    )
                }
                val chosenAirline = airlinePair.value.first
                if (flightNumber == "" || chosenAirline == null) {
                    SearchButton(false) {}
                } else { //User selected Search by Airport
                    val searchString = chosenAirline.iata + flightNumber
                    SearchButton(true) { navigator.navigate(FlightInfoViewDestination(searchString)) }
                }
            } else {
                AirportSearchBox(
                    navigator,
                    homeViewModel.airportResults.collectAsState().value,
                    "Airport (ex. YYZ, Pearson International)"
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
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
                    val savedItems =
                        (state as UiState.Loaded<Pair<List<SavedFlightEntity>, List<SavedAirportEntity>>>).data
                    if (savedItems.first.isEmpty() && savedItems.second.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                "Nothing saved yet!",
                                modifier = Modifier.background(Color(0x99F6F2FA), RectangleShape),
                                fontFamily = bodyFont,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 500.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            items(savedItems.first) { flight ->
                                SavedFlightCard(
                                    Json.decodeFromString<FlightInfo>(flight.json),
                                    navigator
                                )
                            }
                            items(savedItems.second) { airport ->
                                SavedAirportCard(
                                    airportInfo = Json.decodeFromString<Airport>(
                                        airport.json
                                    ), navigator = navigator
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Navigates to correct destination if specified in activity intent
fun handleIntents(activity: Activity, navigator: DestinationsNavigator) {
    val intent = activity.intent
    val destination = when (intent?.action) {
        "FlightInfoView" -> FlightInfoViewDestination(
            flightIata = intent.getStringExtra("flightIata")!!,
            date = LocalDate.parse(intent.getStringExtra("date")!!),
        )
        else -> null
    }
    if (destination != null) {
        navigator.navigate(destination)
    }
}

@Preview
@Composable
fun Preview() = HomeView(EmptyDestinationsNavigator)
