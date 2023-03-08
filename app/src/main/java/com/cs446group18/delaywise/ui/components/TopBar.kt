package com.cs446group18.delaywise.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.cs446group18.delaywise.ui.destinations.HomeViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigator: DestinationsNavigator) {
    val scope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        title = {
            IconButton(onClick = {
                scope.launch {
                    navigator.navigate(HomeViewDestination)
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Flight,
                    contentDescription = "Home Button",
                    modifier = Modifier.rotate(90f)
                )
            }
        }, navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    navigator.popBackStack()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    )
}
