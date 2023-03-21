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
fun LabeledCongestionGraph(navigator: DestinationsNavigator, keys: List<String>, values: List<Int>) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
                        .padding(4.dp, 4.dp, 28.dp, 4.dp)
                )
            CongestionGraph(navigator = navigator, keys, values)
        }
    }
}

fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

@Preview
@Composable
fun PreviewLabeledCongestionGraph() = LabeledCongestionGraph(
    navigator = EmptyDestinationsNavigator,
    List<String>(1){"9am"},
    List<Int>(1){15}
)
