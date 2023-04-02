package com.cs446group18.delaywise.ui.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs446group18.delaywise.R
import com.cs446group18.delaywise.ui.components.BottomBar
import com.cs446group18.delaywise.ui.styles.bodyFont
import com.cs446group18.delaywise.ui.styles.headingFont
import com.cs446group18.delaywise.util.UiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsView(
    navigator: DestinationsNavigator,
    settingsViewModel: SettingsViewModel = viewModel { SettingsViewModel() }
) {
    var parentApiKey =  settingsViewModel.apiKey.collectAsState().value
    val checkedState = remember { mutableStateOf(true) }
    val context = LocalContext.current
    if(parentApiKey is UiState.Loaded<String>){
        Scaffold(bottomBar = { BottomBar(navigator) }) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        painter = painterResource(id = R.drawable.homepage_bg),
                        contentScale = ContentScale.FillHeight,
                    )
                    .padding(top = 70.dp)
                    .padding(horizontal = 30.dp)

            ) {
                Text("Settings", fontSize = 40.sp, fontFamily = headingFont)
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Enable Push Notifications", fontFamily = bodyFont, fontSize = 15.sp)
                        Switch(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextField(
                        value = parentApiKey.data,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color(R.color.main_blue).copy(
                                alpha = 1F
                            )
                        ),
                        onValueChange = {
                            settingsViewModel.onApiKeyChange(it)
                        },
                        placeholder = { Text("Optional: Add API Key") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp), horizontalArrangement = Arrangement.SpaceAround
                ) {
                    PressIconButton(
                        onClick = {
                            Toast.makeText(context, "API key changed!", Toast.LENGTH_SHORT).show()
                            settingsViewModel.setApiKey()
                        },
                        icon = { R.drawable.__plane_icon },
                        text = { Text("Add API Key", fontFamily = bodyFont, fontSize = 15.sp) }
                    )

                }
            }
        }
    }

}

@Preview
@Composable
fun Preview() = SettingsView(EmptyDestinationsNavigator)

@Composable
fun PressIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource =
        remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        onClick, modifier = modifier,
        interactionSource = interactionSource
    ) {
        AnimatedVisibility(visible = isPressed) {
            if (isPressed) {
                Row {
                    icon()
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                }
            }
        }
        text()
    }
}