package com.cs446group18.delaywise.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.components.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
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
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeView(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(30.dp)
        ) {
            Text("Welcome to", fontFamily = appFontFamily, fontSize = 20.sp)
            Text("DelayWise!", fontFamily = appFontFamily, fontSize = 40.sp)
            Text("Enter a flight number airline, or airport:")
            SearchBox()
            Spacer(modifier = Modifier.height(20.dp))
            Text("Saved Flights", fontSize = 28.sp, fontFamily = appFontFamily)
            ScrollableMenu(
                listOf(
                    SavedFlightCard(
                        "LH1810",
                        "Likely 1h Delay",
                        DelayType.LIKELY,
                        "MUC(Munich)",
                        "BCN(Barcelona)",
                        "Mon 21 March, 2022"
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun Preview() = HomeView(EmptyDestinationsNavigator)
