package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.ui.flightinfo.FlightInfoViewModel

@Composable
fun FlightGateCard(flightGateInfoData: FlightInfoViewModel.FlightGateInfo) {
    Card(
        elevation = CardDefaults.cardElevation(15.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Card(
                    elevation = CardDefaults.cardElevation(15.dp),
                    shape = RoundedCornerShape(size = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .padding(3.dp)
                        .width(120.dp)
                        .clickable { }
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {}
                    Text(fontSize = 15.sp, modifier = Modifier.padding(start = 25.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray,
                            )
                        ) {
                            append("Terminal:")
                        }
                    })
                    Text(fontSize = 24.sp, modifier = Modifier.padding(start = 45.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W900,
                                color = Color.Black,
                            )
                        ) {
                            append(flightGateInfoData.terminal)
                        }
                    })
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {}
                }
            }
            Column(
                modifier = Modifier.padding(25.dp)
            ) {

            }
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(15.dp),
                    shape = RoundedCornerShape(size = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .padding(3.dp)
                        .width(120.dp)
                        .clickable { }
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {}
                    Text(fontSize = 15.sp, modifier = Modifier.padding(start = 45.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray,
                            )
                        ) {
                            append("Gate:")
                        }
                    })
                    Text(fontSize = 24.sp, modifier = Modifier.padding(start = 45.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W900,
                                color = Color.Black,
                            )
                        ) {
                            append(flightGateInfoData.gate)
                        }
                    })
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {}
                }
            }
        }

    }
}
@Preview
@Composable
fun PreviewFlightGateCard() = FlightGateCard(
    flightGateInfoData = FlightInfoViewModel.FlightGateInfo(
        "2",
        "K6",
    )
)