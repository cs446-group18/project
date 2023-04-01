package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs446group18.delaywise.ui.destinations.HomeViewDestination
import com.cs446group18.delaywise.ui.styles.headingFont
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigator: DestinationsNavigator) {
    val scope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color(0x00000000)),
        title = {
//            IconButton(onClick = {
//                scope.launch {
//                    navigator.navigate(HomeViewDestination)
//                }
//            }) {
//                Icon(
//                    imageVector = Icons.Filled.Home,
//                    tint = Color(0xFF1B33B4),
//                    contentDescription = "Home Button",
//                    modifier = Modifier.size(35.dp)
//                )
//            }
         },
//        navigationIcon = {
//            IconButton(onClick = {
//                scope.launch {
//                    navigator.popBackStack()
//                }
//            }) {
//                Icon(
//                    imageVector = Icons.Filled.ArrowBack,
//                    contentDescription = "Go Back"
//                )
//            }
//        },
//        actions = {
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(
//                    imageVector = Icons.Filled.Settings,
//                    contentDescription = "Settings"
//                )
//            }
//        }
    )
}

@Preview
@Composable
fun TopBarPreview() = TopBar(EmptyDestinationsNavigator)
