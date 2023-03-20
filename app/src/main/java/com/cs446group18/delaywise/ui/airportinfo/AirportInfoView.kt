package com.cs446group18.delaywise.ui.airportinfo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.components.LabeledCongestionGraph
import com.cs446group18.delaywise.ui.components.TopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import java.util.*
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AirportInfoView(
    navigator: DestinationsNavigator,
    keys: List<String>, values: List<Int>
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
            LabeledCongestionGraph(navigator, keys, values)
        }
    }
}

fun fakeApi() {
    val rightNow = Calendar.getInstance()
    val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
    val times = mutableListOf<String>()
    for (i in 8 downTo 0 step 1) {
        val curr = currentHourIn24Format - i
        times.add((if (curr % 12 === 0) "12" else (curr % 12)).toString() + if (curr >= 12) "PM" else "AM")
    }

    val randList = List(9) { Random.nextInt(0, 180) }
}


@Preview
@Composable
fun Preview() = AirportInfoView(EmptyDestinationsNavigator, mutableListOf<String>(), mutableListOf<Int>())