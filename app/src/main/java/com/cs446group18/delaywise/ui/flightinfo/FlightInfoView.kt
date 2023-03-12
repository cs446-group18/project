package com.cs446group18.delaywise.ui.flightinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.components.FlightInfoCard
import com.cs446group18.delaywise.ui.components.TopBar
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
            modifier = Modifier.padding(contentPadding) ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {Text(
                "AC8836",
                fontFamily = appFontFamily,
                fontSize = 32.sp,
                modifier = Modifier.absolutePadding(left = 10.dp)
            )
            FlightInfoCard(
                flightInfoData = FlightInfoViewModel.FlightInfo(
                    20,
                    "2h10m",
                    "LU256",
                    "MUC",
                    "BCN",
                    "Munich",
                    "Barcelona",
                    "10:55",
                    "12:55",
                    "Mon 21 Mar",
                    "Mon 21 Mar",
                    "2",
                    "K6",
                    "15",
                    "A21"
            ))


        }
    }
}

@Preview
@Composable
fun PreviewFlightInfoView() = FlightInfoView(EmptyDestinationsNavigator)
