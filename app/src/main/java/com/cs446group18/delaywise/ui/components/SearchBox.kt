package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


suspend fun mockApi(searchText: String): List<String> {
    delay(500L) // synthetic delay
    return listOf("$searchText A", "$searchText B", "$searchText C")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox() {
    var searchInput by remember {
        mutableStateOf("")
    }
    var searchResults by remember {
        mutableStateOf(listOf<String>())
    }
    val scope = rememberCoroutineScope()
    Column {
        TextField(
            value = searchInput,
            onValueChange = {
                searchInput = it
                scope.launch {
                    searchResults = mockApi(it)
                }
            },
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth(),
        )
        for (result in searchResults) {
            Card(
                modifier = Modifier
                    .heightIn(min = 50.dp)
                    .fillMaxWidth()
                    .padding(3.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(result)
                }
            }
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Click me!")
        }
    }
}

@Preview
@Composable
fun Preview() = SearchBox()
