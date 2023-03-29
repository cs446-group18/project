package com.cs446group18.delaywise.ui.flightinfo

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.model.getAirlineName
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
                    val dates = listOf("04/29", "04/30", "05/1")
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp,Alignment.CenterHorizontally)) {
                        Text(
                            "${flightInfo.getAirlineName() ?: flightInfo.operator_iata} ${flightInfo.flight_number}",
                            fontFamily = headingFont,
                            fontSize = 32.sp,
                            modifier = Modifier.absolutePadding(left = 10.dp)
                        )
                        displaySaveToggleButton(
                            passedModifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                        DateSelector(dates = dates)
                    }
                    FlightInfoUI(
                        flightInfoData = flightInfo
                    )
                }
            }
        }
    }
}

@Composable
fun DateSelector(dates : List<String>) {
    val contextForToast = LocalContext.current.applicationContext

    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        // clock icon
        IconButton(onClick = {
            expanded = true
        }) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Open Options"
            )
        }

        // drop down menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            // adding items
            dates.forEach {itemValue ->
                DropdownMenuItem(
                    onClick = {
                        Toast.makeText(contextForToast, itemValue, Toast.LENGTH_SHORT)
                            .show()
                        expanded = false
                    },
                    text = { Text(text = itemValue) }
                )
            }
        }
    }
}

//Below code referenced from: https://www.geeksforgeeks.org/icon-toggle-button-in-android-using-jetpack-compose/
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun displaySaveToggleButton(passedModifier: Modifier) {
    val savedState= remember {mutableStateOf(false)}
    IconToggleButton(
        checked = savedState.value,
        onCheckedChange = {
            savedState.value = !savedState.value
            if (savedState.value) {

            }
            else {

            }
        },
        modifier = passedModifier
    ) {
        val transition = updateTransition(savedState.value, "isSaved: {$savedState.value}")

        // on below line we are creating a variable for
        // color of our icon
        val tint by transition.animateColor(label = "iconColor") { isSaved ->
            // if toggle button is checked we are setting color as red.
            // in else condition we are setting color as black
            if (isSaved) Color.Red else Color.DarkGray
        }

        // om below line we are specifying transition
        val size by transition.animateDp(
            transitionSpec = {
                // on below line we are specifying transition
                if (false isTransitioningTo true) {
                    // on below line we are specifying key frames
                    keyframes {
                        // on below line we are specifying animation duration
                        durationMillis = 250
                        // on below line we are specifying animations.
                        30.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
                        35.dp at 15 with FastOutLinearInEasing // for 15-75 ms
                        40.dp at 75 // ms
                        35.dp at 150 // ms
                    }
                } else {
                    spring(stiffness = Spring.StiffnessVeryLow)
                }
            },
            label = "size"
        ) { 30.dp }

        Icon(
            imageVector = if (savedState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Icon",
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}

@Preview
@Composable
fun PreviewFlightInfoView() = FlightInfoView(EmptyDestinationsNavigator, "AC8838")
