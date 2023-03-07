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


enum class DelayType {
    DELAYED, LIKELY, ONTIME
}

val delayTypeColor: Map<DelayType, Color> = mapOf(
    DelayType.DELAYED to Color(0xffBF0000),
    DelayType.LIKELY to Color(0xffFF9900),
    DelayType.ONTIME to Color(0xff00BF1F)
)



//fontWeight = FontWeight.W500,
//color = Color(0xffFF9900),
//fontSize = 12.sp
@Composable
fun SavedFlightCard(flightNumber: String, delayText: String, delayType: DelayType, departAirport: String, arrivalAirport: String, dateDepart: String) {
    Card(
        elevation = CardDefaults.cardElevation(15.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { },
    ) {
        Row(
            modifier = Modifier.padding(all = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(R.drawable.__plane_icon),
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
                            append(flightNumber)
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
                                    color = delayTypeColor[delayType] ?: Color.Black,
                                    fontSize = 12.sp
                                )
                            ) {
                                append(delayText)
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
                            append(departAirport)
                        }
                    })
                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.__plane_icon),
                        contentDescription = null
                    )

                    Text(modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray
                            )
                        ) {
                            append(arrivalAirport)
                        }
                    })
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(Icons.Sharp.NavigateNext, contentDescription = null, modifier = Modifier.size(19.dp), tint = Color.LightGray)
                    }
                }
                Text(modifier = Modifier.padding(), text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.LightGray
                        )
                    ) {
                        append(dateDepart)
                    }
                })
            }
        }
    }
}

@Preview
@Composable
fun PreviewSavedFlight() = SavedFlightCard("LH1810", "Likely 1h Delay", DelayType.LIKELY, "MUC(Munich)", "BCN(Barcelona)", "Mon 21 March, 2022")