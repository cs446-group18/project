package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalAirport
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
import com.cs446group18.delaywise.util.formatAsTime
import com.cs446group18.lib.models.Airport
import com.cs446group18.lib.models.FlightInfo
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


@Composable
fun SavedAirportCard(airportInfo: Airport, navigator: DestinationsNavigator) {
    val scope = rememberCoroutineScope()
    Card(
        elevation = CardDefaults.cardElevation(15.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clickable {
                scope.launch {
                    navigator.navigate(
                        com.cs446group18.delaywise.ui.destinations.AirportInfoViewDestination(
                            airportInfo.code_iata
                        )
                    )
                }
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
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.airport_icon),
                    contentDescription = "Saved Airport"
                )
            }
            Column(
                modifier = Modifier.padding(start = 5.dp, top = 15.dp, bottom = 15.dp, end = 15.dp)
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
                            append("Airport: " + airportInfo.name + " (${airportInfo.code_iata})")
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
                            append(airportInfo.city + ", ")
                        }
                    })
                    Text(modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray
                            )
                        ) {
                            append(airportInfo.timezone)
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(modifier = Modifier.padding(), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.DarkGray
                            )
                        ) {
                            append("Busy: | ") //todo: adding this delay data
                        }
                    })
                    Text(modifier = Modifier.padding(), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.DarkGray
                            )
                        ) {
                            append("Avg Delay: ") //todo: adding this delay data
                        }
                    })
                }
            }
        }
    }
}