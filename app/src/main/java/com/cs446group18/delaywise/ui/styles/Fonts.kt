package com.cs446group18.delaywise.ui.styles

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R

val headingFont = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.montserrat_extrabold,
            weight = FontWeight.W900,
            style = FontStyle.Normal
        ),
    )
)
val bodyFont = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.montserrat_regular,
            weight = FontWeight.W400,
            style = FontStyle.Normal
        ),
    )
)


@Composable
fun Heading(text: String, textAlign: TextAlign? = null,) {
    Text(
        text = text,
        fontFamily = headingFont,
        fontSize = 16.sp,
        textAlign = textAlign,
    )
}

@Composable
fun LargeHeading(text: String) {
    Text(
        text = text,
        fontFamily = headingFont,
        fontSize = 18.sp,
//      textAlign = TextAlign.Center,
    )
}


@Composable
fun BodyText(
    text: String,
    textAlign: TextAlign? = null,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        text = text,
        fontFamily = bodyFont,
        fontSize = 16.sp,
        textAlign = textAlign,
        color = color,
        style = style,
    )
}
