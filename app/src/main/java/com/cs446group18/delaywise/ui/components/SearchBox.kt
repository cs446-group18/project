package com.cs446group18.delaywise.ui.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ListItem(val name: String)

@Composable
fun ListItem(item: ListItem, onSearchInputChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            //.heightIn(min = 50.dp)
            .fillMaxWidth()
            .clickable { onSearchInputChange(item.name) },
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
val mockData = listOf(ListItem("AC8838"), ListItem("AC8839"), ListItem("AF4031"), ListItem("DL4980"), ListItem("stuff1"),ListItem("stuff2"),ListItem("stuff3"),ListItem("stuff4"),ListItem("stuff5"),ListItem("stuff6"),ListItem("stuff7"),ListItem("stuff8"),ListItem("stuff9"),ListItem("stuff10"))
suspend fun mockApi(searchText: TextFieldValue): List<ListItem> {
    delay(500L) // synthetic delay

    return mockData.filter{
        (listItem) -> listItem.lowercase().contains(searchText.text.lowercase())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox() {
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

    var isExpanded by remember { mutableStateOf(false) }

    Column (Modifier
//        .pointerInput(Unit) {
//            // Handle the pointer events
//            awaitPointerEventScope {
//                val event = awaitPointerEvent()
//                if (event.changes.any {it.changedToUp()}) {
//                    if (!isExpanded) {
//                        if (!event.changes.any { it.id == pointerId && it.position in bounds }) {
//                            isExpanded = true
//                        }
//                    }
//                }
//                else {
//                    isExpanded = true
//                }
//            }
//        }
    ) {
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
            modifier = Modifier.fillMaxWidth(),
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 220.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        ) {
            items(searchResults) { item ->
                ListItem(item, onSearchInputChange = {
                    textFieldValueState = TextFieldValue(
                        text = it,
                        selection = TextRange(it.length)
                    )
                })
            }
        }
    }
}
@Preview
@Composable
fun PreviewBox() = SearchBox()
