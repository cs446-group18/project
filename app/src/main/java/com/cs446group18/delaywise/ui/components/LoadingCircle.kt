package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.R

@Composable
fun LoadingCircle() {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()){
        Image(
            modifier = Modifier
                .size(50.dp),
            painter = painterResource(id = R.drawable.__plane_icon),
            contentDescription = null
        )
        CircularProgressIndicator(
            color = Color(0xFF1B33B4),
            modifier = Modifier.size(100.dp)
        )
    }
}

@Preview
@Composable
fun LoadingCirclePreview() = LoadingCircle()