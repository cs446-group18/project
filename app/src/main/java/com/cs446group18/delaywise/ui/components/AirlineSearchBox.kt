package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.model.Airline
import com.cs446group18.delaywise.ui.styles.BodyText
import com.cs446group18.delaywise.ui.styles.bodyStyle
import kotlinx.coroutines.launch

data class AirlineSearchBoxItem(val airline: Airline, val displayText: String)
@Composable
fun AirlineSearchBoxItem(item: AirlineSearchBoxItem, onSelect: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(item.displayText) },
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
                item.displayText,
                textAlign = TextAlign.Left,
                modifier = Modifier.absolutePadding(7.dp, 0.dp, 7.dp, 0.dp)
            )
        }
    }
}


suspend fun filterResults(searchText: String, optionLists: List<AirlineSearchBoxItem>): List<AirlineSearchBoxItem> {
    return optionLists.filter{
        it.displayText.contains(searchText, ignoreCase = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.AirlineSearchBox(airlines: List<Airline>, mutableState: MutableState<Pair<Airline?, String>>) {
    var expanded by remember { mutableStateOf(false) }
    var dropDownWidth by remember { mutableStateOf(0) }
    val suggestions = mutableListOf<Pair<Airline, String>>()

    var searchResults by remember {
        mutableStateOf(listOf<AirlineSearchBoxItem>())
    }

    var processedRawSearchResults = mutableListOf<AirlineSearchBoxItem>()
    for (item in airlines) {
        val displayText = item.iata + "/" + item.icao + " || " + item.airline
        processedRawSearchResults.add(AirlineSearchBoxItem(item, displayText))
    }

    var showDropDown by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.weight(0.65f, true)){
        TextField(
            value = mutableState.value.second,
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            textStyle = bodyStyle,
            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.Black, containerColor = Color.White,
                focusedBorderColor = Color(
                R.color.main_blue).copy(
                alpha = 1F
            )),
            onValueChange = {
                mutableState.value = Pair(mutableState.value.first, it)
                scope.launch {
                    searchResults = filterResults(mutableState.value.second, processedRawSearchResults)
                }},
            placeholder = { BodyText("Airline (ex. DAL, Delta)") },
            modifier = Modifier
                .onFocusChanged {
                    showDropDown = it.isFocused
                }
        )

        if (showDropDown) {
            LazyColumn(
                modifier = Modifier
                    .width(220.dp)
                    .heightIn(max = 220.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                items(searchResults) { item ->
                    AirlineSearchBoxItem(item, onSelect = {
                        mutableState.value = Pair(item.airline, item.displayText)
                        TextFieldValue(
                            text = item.displayText,
                            selection = TextRange(item.displayText.length)
                        )
                    })
                }
            }
        }
    }
}