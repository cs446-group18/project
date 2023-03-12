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
import com.cs446group18.lib.models.FlightInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// TODO: move to ui.flightinfo package because it's specific to that screen
@Composable
fun FlightInfoCard(flightInfoData: FlightInfo) {
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
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.padding( start = 25.dp, end = 15.dp,  bottom = 5.dp)
                ) {
                    Text(
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black,
                                )
                            ) {
                                append("Delayed: ${flightInfoData.delay ?: 0} min")
                            }
                        })
                }
                Column(
                    modifier = Modifier.padding(start = 45.dp , bottom = 5.dp)
                ) {
                    Text(
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
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
        ) {
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
                        Text(
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 5.dp),
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                    )
                                ) {
                                    append(flightInfoData.depAirportName + " : " + flightInfoData.depCity)
                                }
                            })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 5.dp),
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Gray
                                    )
                                ) {
                                    val scheduled = flightInfoData.depScheduled
                                    if (scheduled != null) {
                                        append(scheduled.format(DateTimeFormatter.ISO_LOCAL_TIME) ?: "")
                                    }
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
                        Text(
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 5.dp),
                            text = buildAnnotatedString {
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
                        Text(
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 10.dp),
                            text = buildAnnotatedString {
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
                        Text(
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 10.dp),
                            text = buildAnnotatedString {
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
                        Text(
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 5.dp),
                            text = buildAnnotatedString {
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
                                append(flightInfoData.depTerminal ?: "??")
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
                        Text(
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 5.dp),
                            text = buildAnnotatedString {
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
                                append(flightInfoData.depGate ?: "??")
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
    ) {
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
                    Text(
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                )
                            ) {
                                append(flightInfoData.arrAirportName + " : " + flightInfoData.arrCity)
                            }
                        })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray
                                )
                            ) {
                                val scheduled = flightInfoData.depScheduled
                                if (scheduled != null) {
                                    append(scheduled.format(DateTimeFormatter.ISO_LOCAL_TIME))
                                }
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
                    Text(
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
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
                    Text(fontSize = 16.sp,modifier = Modifier.padding(start = 10.dp), text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray

                            )
                        ) {
                            val scheduled = flightInfoData.arrScheduled
                            if (scheduled != null) {
                                append(scheduled.format(DateTimeFormatter.ISO_LOCAL_TIME))
                            }
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
                    Text(
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
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
                            append(flightInfoData.arrTerminal ?: "??")
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
                    Text(
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
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
                            append(flightInfoData.arrGate ?: "??")
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
    flightInfoData = FlightInfo(
        delay = 35,
        flightDuration = 60*2+10,
        flightNumber = "Lufthansa 256 (LU256)",
        depAirportIata = "MUC",
        arrAirportIata = "BCN",
        depCity = "Munich",
        arrCity = "Barcelona",
        depScheduled = LocalDateTime.now(),
        depEstimated = LocalDateTime.now().plusMinutes(5), // late 5 mins
        arrScheduled = LocalDateTime.now().plusHours(1),
        arrEstimated = LocalDateTime.now().plusHours(1).minusMinutes(5), // early 5 mins
        depTerminal = "2",
        depGate = "K6",
        arrTerminal = "15",
        arrGate = "A21",
        flightIata = "LH256",
        airlineIata = "LH",
        airlineName = "Lufthansa",
        arrAirportName = "Munich International Airport",
        depAirportName = "Barcelona International Airport",
    )
)

