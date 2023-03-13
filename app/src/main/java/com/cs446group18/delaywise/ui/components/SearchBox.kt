package com.cs446group18.delaywise.ui.components

import android.util.Log
import android.view.MotionEvent
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.destinations.FlightInfoViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ListItem(val name: String)

@Composable
fun ListItem(item: ListItem, onSelect: (String) -> Unit) {
    Card(
        modifier = Modifier
            //.heightIn(min = 50.dp)
            .fillMaxWidth()
            .clickable { onSelect(item.name) },
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
                item.name,
                textAlign = TextAlign.Left,
                modifier = Modifier.absolutePadding(7.dp, 0.dp, 7.dp, 0.dp)
            )
        }
    }
}
val mockData = listOf(ListItem("AC8836"), ListItem("AC914"),ListItem("AC918"),ListItem("AC1088"), ListItem("AC8839"), ListItem("AC834"), ListItem("LH1810"), ListItem("AC883"))
suspend fun mockApi(searchText: TextFieldValue): List<ListItem> {
    delay(500L) // synthetic delay

    return mockData.filter{
        (listItem) -> listItem.lowercase().contains(searchText.text.lowercase())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(navigator: DestinationsNavigator) {
    var textFieldValueState by remember{
        mutableStateOf(
            TextFieldValue(
                text = ""
            )
        )
    }

    var searchResults by remember {
        mutableStateOf(listOf<ListItem>())
    }
    val scope = rememberCoroutineScope()

    var showDropDown by remember {
        mutableStateOf(false)
    }

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
                    searchResults = mockApi(it)
                }
            },
            placeholder = { Text("ex. AA5555") },
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
                    ListItem(item, onSelect = {
                        textFieldValueState = TextFieldValue(
                            text = it,
                            selection = TextRange(it.length)
                        )
                        navigator.navigate(FlightInfoViewDestination(it))
                    })
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewBox() = SearchBox(EmptyDestinationsNavigator)
