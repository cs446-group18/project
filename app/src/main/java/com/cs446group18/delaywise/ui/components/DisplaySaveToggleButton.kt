package com.cs446group18.delaywise.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


//Below code referenced from: https://www.geeksforgeeks.org/icon-toggle-button-in-android-using-jetpack-compose/
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DisplaySaveToggleButton(id: String, passedModifier: Modifier, isSavedFlow: StateFlow<Boolean>, saveFunc: suspend () -> Unit, deleteFunc: suspend () -> Unit) {
    val scope = rememberCoroutineScope()
    val isSaved by isSavedFlow.collectAsState(false)

    IconToggleButton(
        checked = (isSaved),
        onCheckedChange = {
            scope.launch(Dispatchers.IO) {
                if (!isSaved) {
                    saveFunc()
                } else {
                    deleteFunc()
                }
            }
        },
        modifier = passedModifier
    ) {
        val transition = updateTransition(isSaved, "isSaved: {$isSaved}")

        // on below line we are creating a variable for
        // color of our icon
        val tint by transition.animateColor(label = "iconColor") { isSaved ->
            // if toggle button is checked we are setting color as red.
            // in else condition we are setting color as black
            if (isSaved) Color.Red else Color.DarkGray
        }

        // om below line we are specifying transition
        val size by transition.animateDp(
            transitionSpec = {
                // on below line we are specifying transition
                if (false isTransitioningTo true) {
                    // on below line we are specifying key frames
                    keyframes {
                        // on below line we are specifying animation duration
                        durationMillis = 250
                        // on below line we are specifying animations.
                        30.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
                        35.dp at 15 with FastOutLinearInEasing // for 15-75 ms
                        40.dp at 75 // ms
                        35.dp at 150 // ms
                    }
                } else {
                    spring(stiffness = Spring.StiffnessVeryLow)
                }
            },
            label = "size"
        ) { 30.dp }

        Icon(
            imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Icon",
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}

