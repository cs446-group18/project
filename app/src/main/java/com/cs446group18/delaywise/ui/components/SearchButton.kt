package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.R

@Composable
fun SearchButton(isEnabled: Boolean, onClickParam: () -> Unit) {
    Button(
        onClick = onClickParam,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(R.color.main_blue).copy(alpha = 1F)),
        enabled = isEnabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Find the way")
            Image(
                painter = painterResource(id = R.drawable.__plane_icon_white),
                contentDescription = null,
                modifier = Modifier
                    .absolutePadding(10.dp, 1.dp)
                    .width(20.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewButton() = SearchButton(false) {}