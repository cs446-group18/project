package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableMenu(items: List<@Composable Any>) {
    Column (
        modifier = Modifier.background(color = Color.White.copy(alpha = 0.7F)).fillMaxHeight()
    ){
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        )
        {
            items
        }
    }
}

@Preview
@Composable
fun PreviewScrollableMenu() = ScrollableMenu(
    listOf(
        SavedFlightCard("LH1810", "Likely 1h Delay", DelayType.LIKELY, "MUC(Munich)", "BCN(Barcelona)", "Mon 21 March, 2022")
    )
)