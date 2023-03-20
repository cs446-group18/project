package com.cs446group18.delaywise.ui.airportinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.components.CongestionGraph
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
fun AirportInfoView(
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
                "Airport Info",
                fontFamily = appFontFamily,
                fontSize = 40.sp,
                modifier = Modifier.absolutePadding(left = 10.dp)
            )

            CongestionGraph(navigator = navigator)
        }
    }
}

@Preview
@Composable
fun Preview() = AirportInfoView(EmptyDestinationsNavigator)