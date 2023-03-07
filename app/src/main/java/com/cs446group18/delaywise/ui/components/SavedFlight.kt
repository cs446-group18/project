package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SavedFlight(item: ListItem) {
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(size = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
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
                            append(item.name)
                        }
                    })
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.W500, color = Color(0xffFF9900),
                                    fontSize = 12.sp
                                )
                            ) {
                                append("Likely 1h Delay")
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
                            append("MUC")
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
                            append("BCN")
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
                        append("Mon 21 March, 2022")
                    }
                })
            }
        }
    }
}

suspend fun mockSavedFlightData(): List<ListItem> {
    delay(500L) // synthetic delay
    return listOf(ListItem("AC8838"), ListItem("AC8839"), ListItem("AF4031"), ListItem("DL4980"))
}

@Composable
fun SavedFlightList() {
    var savedFlightData by remember {
        mutableStateOf(listOf<ListItem>())
    };
    val scope = rememberCoroutineScope();

    scope.launch {
        savedFlightData = mockSavedFlightData()
    }
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 150.dp)
        ) { items(savedFlightData) {
            item -> SavedFlight(item)
        }
        }
    }
}

@Preview
@Composable
fun PreviewSavedFlight() = SavedFlightList()