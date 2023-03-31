package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.foundation.gestures.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.model.Airport
import com.cs446group18.delaywise.ui.destinations.AirportInfoViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

data class AirportSearchBoxItem(val airport: Airport, val displayText: String)

@Composable
fun AirportSearchBoxItem(item: AirportSearchBoxItem, onSelect: (String) -> Unit) {
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
suspend fun filterResults(searchText: TextFieldValue, optionLists: List<AirportSearchBoxItem>): List<AirportSearchBoxItem> {
    return optionLists.filter{
       it.displayText.contains(searchText.text, ignoreCase = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportSearchBox(navigator: DestinationsNavigator, airports: List<Airport>, placeHolderText: String) {
    var textFieldValueState by remember{
        mutableStateOf(
            TextFieldValue(
                text = ""
            )
        )
    }

    var searchResults by remember {
        mutableStateOf(listOf<AirportSearchBoxItem>())
    }

    var processedRawSearchResults = mutableListOf<AirportSearchBoxItem>()
    for (item in airports) {
        val displayText = item.iata + "/" + item.icao + " || " + item.airport
        processedRawSearchResults.add(AirportSearchBoxItem(item, displayText))
    }

    var showDropDown by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Column {
        TextField(
            value = textFieldValueState,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color(R.color.main_blue).copy(
                    alpha = 1F
                )
            ),
            onValueChange = {
                textFieldValueState = it
                scope.launch {
                    searchResults = filterResults(it, processedRawSearchResults)
                }
            },
            placeholder = { Text(placeHolderText) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    showDropDown = it.isFocused
                }
        )
        if (showDropDown) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                items(searchResults) { item ->
                    AirportSearchBoxItem(item, onSelect = {
                        println("in AirportSearchBox OnSelect")
                        textFieldValueState = TextFieldValue(
                            text = item.displayText,
                        )
                        navigator.navigate(AirportInfoViewDestination(item.airport.iata))
                    })
                }
            }
        }
    }
}
