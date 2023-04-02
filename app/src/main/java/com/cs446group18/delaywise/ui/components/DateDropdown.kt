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
import com.cs446group18.delaywise.util.formatAsDate
import com.cs446group18.lib.models.FlightInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDropdown(suggestions: List<LocalDate>, defaultDate: Instant, changeDateFunc: suspend (LocalDate) -> Unit, isReadOnly: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    var dropDownWidth by remember { mutableStateOf(0) }
    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    var mutableState by remember { mutableStateOf(defaultDate.formatAsDate())}
    val scope = rememberCoroutineScope()

    Column {
        OutlinedTextField(
            value = mutableState,
            onValueChange = { mutableState = it },
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
            suggestions.forEach { suggestion ->
                DropdownMenuItem(onClick = {
                    mutableState = suggestion.toString()
                    scope.launch(Dispatchers.IO) { changeDateFunc(suggestion) }
                    expanded = false
                }, text = { Text(suggestion.toString()) })
            }
        }
    }
}