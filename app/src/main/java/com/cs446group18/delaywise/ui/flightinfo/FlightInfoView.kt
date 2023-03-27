package com.cs446group18.delaywise.ui.flightinfo

import android.widget.Toast
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
                            "${flightInfo.airlineName} ${flightInfo.flightNumber}",
                            fontFamily = headingFont,
                            fontSize = 32.sp,
                            modifier = Modifier.absolutePadding(left = 10.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            tint = Color(0xFF2096F3),
                            contentDescription = "Save Page Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                                .clickable(onClick = {})
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

@Preview
@Composable
fun PreviewFlightInfoView() = FlightInfoView(EmptyDestinationsNavigator, "AC8838")
