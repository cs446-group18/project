package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSmall(suggestions: List<String>, mutableState: MutableState<String>, isReadOnly: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    var dropDownWidth by remember { mutableStateOf(0) }
    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    Column {
        OutlinedTextField(
            value = mutableState.value,
            onValueChange = { mutableState.value = it },
            readOnly = isReadOnly,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    dropDownWidth = it.width
                },
            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Black, containerColor = Color.White, focusedBorderColor = Color(
                R.color.main_blue).copy(
                alpha = 1F
            )),
            trailingIcon = {
                Icon(icon,"contentDescription", Modifier.clickable { expanded = !expanded })
            },
            textStyle = TextStyle(fontSize = 14.sp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){dropDownWidth.toDp()})
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