package com.cs446group18.delaywise.ui.settings

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ApiKeyEntity
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _apiKeyState = MutableStateFlow<UiState<String>>(UiState.Loading())
    val apiKeyState: StateFlow<UiState<String>> = _apiKeyState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiKeys = ClientModel.getInstance().apiKeyDao.listApiKeys()
                val apiKey = apiKeys.getOrNull(0)?.id ?: ""

                _apiKeyState.value = UiState.Loaded(apiKey)
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _apiKeyState.value = UiState.Error(ex.toString())
            }
        }
    }

    suspend fun updateApiKey(value: String, activity: Activity) {
        _apiKeyState.value = UiState.Loaded(value)
        if(value.length == 32) {
            ClientModel.getInstance().apiKeyDao.clear()
            ClientModel.getInstance().apiKeyDao.insert(ApiKeyEntity(value))
            ClientModel.getInstance().fetcher.apiKey = value
            activity.runOnUiThread {
                Toast.makeText(activity, "Saved new API key!", Toast.LENGTH_SHORT).show()
            }
        } else if(value.isEmpty()) {
            ClientModel.getInstance().apiKeyDao.clear()
            ClientModel.getInstance().fetcher.apiKey = null
            activity.runOnUiThread {
                Toast.makeText(
                    activity,
                    "Reset API key, now routing requests to shared server",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
