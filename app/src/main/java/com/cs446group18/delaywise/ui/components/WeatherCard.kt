package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.flightinfo.FlightInfoViewModel
import com.cs446group18.delaywise.ui.home.HomeViewModel
import com.cs446group18.delaywise.ui.styles.BodyText
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

enum class WeatherType {
    CLEAR_DAY,
    CLEAR_NIGHT,
    CLOUDY_DAY,
    CLOUDY_NIGHT,
    FOG,
    FREEZING_RAIN,
    HAZE,
    HEAVY_RAIN,
    HEAVY_SNOW,
    RAIN_NIGHT,
    RAIN_DAY,
    THUNDERSTORM,
    WINDY
}

private val weatherMap = mapOf(
    "default" to R.drawable.cloudy_day_icon,
)

@Composable
fun WeatherCard(weatherData: FlightInfoViewModel.WeatherData, navigator: DestinationsNavigator) {
    val scope = rememberCoroutineScope()
    val weatherAsset =
        if (weatherMap.containsKey(weatherData.weather)) weatherMap[weatherData.weather] else weatherMap["default"];
    Card(
        elevation = CardDefaults.cardElevation(15.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(3.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.padding(all = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) { BodyText(weatherData.date) }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(weatherAsset!!),
                alignment = Alignment.Center,
                contentDescription = null
            )
        }
    }
    }
}
@Preview
@Composable
fun PreviewWeatherCard() = WeatherCard(
    weatherData = FlightInfoViewModel.WeatherData(
        "Wednesday",
        "Sunny")
    , navigator = EmptyDestinationsNavigator

)