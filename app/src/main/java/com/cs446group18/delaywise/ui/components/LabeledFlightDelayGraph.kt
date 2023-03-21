package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
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

@Composable
fun LabeledFlightDelayGraph(navigator: DestinationsNavigator, keys: List<String>, values: List<Int>) {
    Column(
        modifier = Modifier.height(260.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Average Departure Delay",
            fontFamily = appFontFamily,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(4.dp)
        )
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                "Minutes Delayed",
                fontFamily = appFontFamily,
                fontSize = 15.sp,
                modifier = Modifier
                    .rotate(-90f)
                    .vertical()
                    .padding(4.dp, 4.dp, 13.dp, 4.dp)
            )
            FlightDelayGraph(navigator = navigator, keys, values)
        }
    }
}

@Preview
@Composable
fun PreviewLabeledFlightDelayGraph() = LabeledFlightDelayGraph(
    navigator = EmptyDestinationsNavigator,
    mutableListOf<String>("03-21", "03-22", "03-23", "03-24", "03-25", "03-26", "03-27", "03-28", "03-29", "03-30"),
    mutableListOf<Int>(1,2, 3, 2, 2, 1, 1, 2, 3,7)
)
