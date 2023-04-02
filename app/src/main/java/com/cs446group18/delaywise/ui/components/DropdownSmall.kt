package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.styles.bodyFont
import com.cs446group18.delaywise.ui.styles.bodyStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.DropdownSmall(
    suggestions: List<String>,
    mutableState: MutableState<String>,
    isReadOnly: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var dropDownWidth by remember { mutableStateOf(0) }
    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    Column(modifier = Modifier.weight(1f, false)) {
        OutlinedTextField(
            value = mutableState.value,
            onValueChange = { mutableState.value = it },
            shape = RoundedCornerShape(8.dp),
            readOnly = isReadOnly,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .onSizeChanged {
                    dropDownWidth = it.width
                },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                containerColor = Color.White,
                focusedBorderColor = Color(R.color.main_blue).copy(alpha = 1F)
            ),
            trailingIcon = {
                Icon(icon, "contentDescription")
            },
            textStyle = bodyStyle,
            // TextFields aren't clickable by default: https://stackoverflow.com/a/70335041/7062267
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                expanded = !expanded
                            }
                        }
                    }
                },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { dropDownWidth.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    mutableState.value = label
                    expanded = false
                }, text = { Text(label) })
            }
        }
    }
}