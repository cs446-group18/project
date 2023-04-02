package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.styles.bodyStyle
import com.cs446group18.delaywise.util.formatAsDate
import com.cs446group18.lib.models.FlightInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DropdownItem(suggestionText: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .fillMaxSize(0.1F)
        ) {
            Text(
                text = suggestionText,
                style = bodyStyle,
                textAlign = TextAlign.Left,
                modifier = Modifier.absolutePadding(7.dp, 0.dp, 7.dp, 0.dp)
            )
        }
    }
}///
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDropdown(suggestions: List<LocalDate>, defaultDate: Instant, changeDateFunc: suspend (LocalDate) -> Unit, isReadOnly: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    var dropDownWidth by remember { mutableStateOf(0) }
    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    var mutableState by remember { mutableStateOf(defaultDate.formatAsDate())}
    val scope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = mutableState,
            onValueChange = { mutableState = it },
            readOnly = isReadOnly,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .onSizeChanged {
                    dropDownWidth = it.width
                },
            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Black, containerColor = Color.White, focusedBorderColor = Color(
                R.color.main_blue).copy(
                alpha = 1F
            )),
            singleLine = true,
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
            trailingIcon = {
                Icon(icon,"contentDescription")
            },
            textStyle = bodyStyle,
        )
        if (expanded) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(0.6f).heightIn(max = 150.dp),
            ) {
                items(suggestions) { suggestion ->
                    DropdownItem(onClick = {
                        mutableState = suggestion.toString()
                        scope.launch(Dispatchers.IO) { changeDateFunc(suggestion) }
                        expanded = false
                    }, suggestionText = suggestion.toString())
                }
            }
        }
    }
}