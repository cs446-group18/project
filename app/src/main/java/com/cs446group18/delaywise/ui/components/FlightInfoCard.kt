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
fun FlightInfoCard(flightInfoData: FlightInfoViewModel.FlightInfo) {
    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clickable { }
    ) {

        Card(
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(size = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
                .clickable { }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.padding( start = 25.dp, bottom = 5.dp)
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Red,
                            )
                        ) {
                            append(" Expected Delayed: " + flightInfoData.flightDelay + " min")
                        }
                    })
                }
                Column(
                    modifier = Modifier.padding(start = 25.dp , bottom = 5.dp)
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                            )
                        ) {
                            append("Duration: " + flightInfoData.flightDuration)
                        }
                    })
                }
            }
        }

        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(size = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .clickable { }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(fontSize = 18.sp, modifier = Modifier.padding(top = 5.dp, start = 15.dp), text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                        )
                    ) {
                        append("Flight Prediction By Amadeus: ")
                    }
                })

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )
                            ) {
                                append("10-15 min")
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                            ) {
                                append("Predicted")
                            }
                        })
                    }
                }
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )
                            ) {
                                append("45%")
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                            ) {
                                append("Likelihood")
                            }
                        })
                    }
                }

            }
        }
        Card(
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(size = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .clickable { }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(all = 0.dp),
            ) {
                Column(
                    modifier = Modifier.padding(start = 15.dp , top = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 20.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                )
                            ) {
                                append(flightInfoData.departAirport + " : " + flightInfoData.departCity)
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black
                                )
                            ) {
                                append(flightInfoData.departDate)
                            }
                        })
                    }
                }


            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.padding( start = 15.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black
                                )
                            ) {
                                append("Estimated Departure:")
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp,modifier = Modifier.padding(start = 10.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Red
                                )
                            ) {
                                append("12:50")
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp,modifier = Modifier.padding(start = 10.dp), text = buildAnnotatedString {
                            withStyle(
                                    style = SpanStyle(
                                    color = Color.Gray

                                )
                            ) {
                                append("12:30")
                            }
                        })
                    }
                }
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp,modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black
                                )
                            ) {
                                append("Terminal: ")
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(modifier = Modifier.padding(start = 15.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray
                                )
                            ) {
                                append(flightInfoData.departTerminal)
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.White
                                )
                            ) {
                                append("    ")
                            }
                        })
                    }
                }
                Column(
                    modifier = Modifier.padding(25.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black
                                )
                            ) {
                                append("Gate:")
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray
                                )
                            ) {
                                append(flightInfoData.departGate)
                            }
                        })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.White
                                )
                            ) {
                                append("    ")
                            }
                        })
                    }
                }
            }
        }


    }
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .clickable { }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(all = 0.dp),
        ) {
            Column(
                modifier = Modifier.padding(start = 15.dp , top = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 20.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                            )
                        ) {
                            append(flightInfoData.arrivalAirport + " : " + flightInfoData.arrivalCity)
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black
                            )
                        ) {
                            append(flightInfoData.arrivalDate)
                        }
                    })
                }
            }


        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black
                            )
                        ) {
                            append("Estimated Arrival:")
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp,modifier = Modifier.padding(start = 10.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Green
                            )
                        ) {
                            append("12:30")
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 10.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray


                            )
                        ) {
                            append(flightInfoData.arrivalTime)
                        }
                    })
                }
            }
            Column(
                modifier = Modifier.padding(22.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp,modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black
                            )
                        ) {
                            append("Terminal: ")
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(modifier = Modifier.padding(start = 15.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray
                            )
                        ) {
                            append(flightInfoData.arrivalTerminal)
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White
                            )
                        ) {
                            append("    ")
                        }
                    })
                }
            }
            Column(
                modifier = Modifier.padding(25.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black
                            )
                        ) {
                            append("Gate:")
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray
                            )
                        ) {
                            append(flightInfoData.arrivalGate)
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White
                            )
                        ) {
                            append("    ")
                        }
                    })
                }
            }
        }
    }
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .clickable { }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(fontSize = 20.sp, modifier = Modifier.padding(top = 5.dp, start = 15.dp), text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                    )
                ) {
                    append("Historical Delays Over: ")
                }
            })
            Text(fontSize = 20.sp, modifier = Modifier.padding(top = 5.dp, start = 65.dp), text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                    )
                ) {
                    append("7 days")
                }
            })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        ) {
                            append("43%")
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        ) {
                            append("Rate of Delay")
                        }
                    })
                }
            }
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        ) {
                            append("25 min")
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        ) {
                            append("Avg. Delay")
                        }
                    })
                }
            }
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        ) {
                            append("10%")
                        }
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        ) {
                            append("Cancellation Rate")
                        }
                    })
                }
            }
        }
    }


}
@Preview
@Composable
fun PreviewFlightInfoCard() = FlightInfoCard(
    flightInfoData = FlightInfoViewModel.FlightInfo(
        20,
        "2h10m",
    "Lufthansa 256 (LU256)",
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

    )
)

