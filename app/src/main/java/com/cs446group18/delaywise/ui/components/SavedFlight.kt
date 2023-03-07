package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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
import com.cs446group18.delaywise.R

@Composable
fun SavedFlight() {
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
                Image(modifier = Modifier.size(40.dp), painter = painterResource(R.drawable.__plane_icon), contentDescription = null)
            }
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W800,
                                color = Color.Black,
                            )
                        ) {
                            append("Lufthansa - LH 1810")
                        }
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray
                                )
                            ) {
                                append("MUC (Munich)")
                            }
                        }
                    )
                    Image(
                        modifier = Modifier.size(20.dp).padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.__plane_icon),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray
                                )
                            ) {
                                append("BCN (Barcelona)")
                            }
                        }
                    )
                }
                Text(
                    modifier = Modifier.padding(),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.LightGray
                            )
                        ) {
                            append("Mon 21 March, 2022")
                        }
                    }
                )
            }
        }
    }
//        ExtendedFloatingActionButton(
//            icon = {Icon(painterResource(id = R.drawable.__plane_icon_white), null)},
//            text = { Text("FloatingActionButton") },
//            onClick = { /*do something*/ },
//            elevation = FloatingActionButtonDefaults.elevation(8.dp),
//            containerColor = Color.White,
//            shape = RectangleShape,
//        )
}

@Preview
@Composable
fun PreviewSavedFlight() = SavedFlight()