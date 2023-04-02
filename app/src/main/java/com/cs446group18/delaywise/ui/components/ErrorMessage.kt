package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R

private val headingFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.montserrat_extrabold,
            weight = FontWeight.W900,
            style = FontStyle.Normal
        ),
    )
)

private val bodyFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.montserrat_regular,
            weight = FontWeight.W400,
            style = FontStyle.Normal
        ),
    )
)

@Composable
fun ErrorMessage(errorMsg: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Service unavailable",
            fontFamily = headingFontFamily,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Text(
            "Please try again later",
            fontFamily = headingFontFamily,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.sad_face),
            contentDescription = null
        )
        Text(
            "Error Message: {$errorMsg}",
            fontFamily = bodyFontFamily,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PreviewErrorMessage() = ErrorMessage("Error Code: 500")