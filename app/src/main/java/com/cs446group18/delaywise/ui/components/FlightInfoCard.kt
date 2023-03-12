package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
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
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(fontSize = 18.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                            )
                        ) {
                            append("Delayed: " + flightInfoData.flightDelay + " min")
                        }
                    })
                }
                Column(
                    modifier = Modifier.padding(23.dp)
                ) {
                    Text(fontSize = 18.sp, modifier = Modifier.padding(end = 5.dp), text = buildAnnotatedString {
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
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
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
                                    color = Color.Gray
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
                                append("10:00")
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
                                append("2")
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
                                append("K6")
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
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
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
                                color = Color.Gray
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
                                color = Color.Gray
                            )
                        ) {
                            append(flightInfoData.arrivalTime)
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
                            append("9:30")
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
                            append("2")
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
                            append("K6")
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
            horizontalArrangement = Arrangement.Center
        ) {
            Text(fontSize = 20.sp, modifier = Modifier.padding(start = 5.dp), text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                ) {
                    append("Historical Delays Over: ")
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
                                color = Color.Gray
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
                                color = Color.Black
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
                                color = Color.Gray
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
                                color = Color.Black
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
                                color = Color.Gray
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
                                color = Color.Black
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
        35,
        "2h10m",
    "Lufthansa 256 (LU256)",
    "MUC",
    "BCN",
    "Munich",
    "Barcelona",
    "10:55",
    "12:55",
    "Mon 21 Mar",
    "Mon 21 Mar"
    )
)

