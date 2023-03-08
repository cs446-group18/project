package com.cs446group18.delaywise.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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

private val savedFlightsList = mutableListOf(
    HomeViewModel.Flight("AC8839", "Delayed", DelayType.DELAYED, "RDU(Raleigh)", "YYZ(Toronto)", "Delayed to 5pm; Wed 15 March, 2023"),
    HomeViewModel.Flight("AC834", "Likely 15m Delay", DelayType.LIKELY, "YYZ(Toronto)", "MUC(Munich)", "Scheduled 8:30pm; Wed 15 March, 2023"),
    HomeViewModel.Flight("LH1810", "On-Time", DelayType.ONTIME, "MUC(Munich)", "BCN(Barcelona)", "Scheduled 9am; Mon 16 March, 2023"),
    HomeViewModel.Flight("AC8838", "Likely 1h Delay", DelayType.LIKELY, "YYZ(Toronto)", "RDU(Raleigh)", "Scheduled 2pm; Mon 21 March, 2023"),
)

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeView(
    navigator: DestinationsNavigator
) {
    val focusManager = LocalFocusManager.current
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
                .padding(contentPadding)
//                .padding(contentPadding.calculateLeftPadding(layoutDirection = z))
//                .padding(contentPadding.calculateRightPadding())
//                .padding(contentPadding.calculateTopPadding())
                .padding(30.dp)
        ) {
            Text("Welcome to", fontFamily = appFontFamily, fontSize = 20.sp)
            Text("DelayWise!", fontFamily = appFontFamily, fontSize = 40.sp)
            Text("Enter a flight number airline, or airport:")
            SearchBox()
            Spacer(modifier = Modifier.height(20.dp))
            Text("Saved Flights", fontSize = 28.sp, fontFamily = appFontFamily)
            LazyColumn(
                modifier = Modifier.fillMaxWidth().heightIn(max = 800.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                items(savedFlightsList) {
                        flight -> SavedFlightCard(flightData = flight)
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() = HomeView(EmptyDestinationsNavigator)
