package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.ui.destinations.FlightInfoViewDestination
import com.cs446group18.delaywise.ui.destinations.SavedFlightsViewDestination
import com.cs446group18.delaywise.ui.flightinfo.FlightInfoViewModel
import com.cs446group18.delaywise.ui.home.HomeViewModel
import com.cs446group18.lib.models.FlightInfo
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


enum class DelayType {
    DELAYED, LIKELY, ONTIME
}

val delayTypeColor: Map<DelayType, Color> = mapOf(
    DelayType.DELAYED to Color(0xffBF0000),
    DelayType.LIKELY to Color(0xffFF9900),
    DelayType.ONTIME to Color(0xff00BF1F)
)

private val airlineMap = mapOf(
    "default" to R.drawable.__plane_icon,
    "LH" to R.drawable.lufthansa,
    "AC" to R.drawable.aircanada
)
@Composable
fun SavedFlightCard(flightInfo: FlightInfo, navigator: DestinationsNavigator) {
    val scope = rememberCoroutineScope()
    val airlineAsset =
        if (airlineMap.containsKey(flightInfo.operator_iata)) airlineMap[flightInfo.operator_iata] else airlineMap["default"];
    Card(
        elevation = CardDefaults.cardElevation(15.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clickable {
                scope.launch { navigator.navigate(FlightInfoViewDestination(flightInfo.flight_number)) }
            }
    ) {
        Row(
            modifier = Modifier.padding(all = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(all = 10.dp)
            ) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(airlineAsset!!),
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W800,
                                color = Color.Black,
                            )
                        ) {
                            append(flightInfo.ident_iata)
                        }
                    })
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.W500,
                                    //todo: color = delayTypeColor[flightData.delayType] ?: Color.Black,
                                    fontSize = 12.sp
                                )
                            ) {
//                                val flightData
//                                todo: append(flightData.delayText) need delay text with "Likely 15m", OnTime, or Delayed
                            }
                        })
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray
                            )
                        ) {
                            append(flightInfo.origin.code_iata + "(${flightInfo.origin.city})")
                        }
                    })
                    Image(
                        modifier = Modifier
                            .size(15.dp)
                            .padding(end = 2.dp),
                        painter = painterResource(id = R.drawable.__plane_icon),
                        contentDescription = null
                    )

                    Text(modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray
                            )
                        ) {
                            append(flightInfo.destination.code_iata + "(${flightInfo.destination.city})")
                        }
                    })
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            Icons.Sharp.NavigateNext,
                            contentDescription = null,
                            modifier = Modifier.size(35.dp),
                            tint = Color.LightGray
                        )
                    }
                }
                Text(modifier = Modifier.padding(), text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.DarkGray
                        )
                    ) {
                        append("Expected Departure:" + flightInfo.getDepartureDate().toString())
                    }
                })
            }
        }
    }
}