package com.cs446group18.delaywise.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientFetcher
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.FlightInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class SettingsViewModel() : ViewModel()
{
    private val _apiKey =
        MutableStateFlow<UiState<String>>(UiState.Loading())
    val apiKey: StateFlow<UiState<String>> = _apiKey

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _apiKey.emit(UiState.Loading())
            try {

                var oldApiKey = (ClientModel.getInstance().model.fetcher as ClientFetcher).apiKey
                _apiKey.value = UiState.Loaded(oldApiKey ?: "")

            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _apiKey.value = UiState.Error(ex.toString())
            }
        }
    }

    fun onApiKeyChange(inputKey: String){
        _apiKey.value = UiState.Loaded(inputKey)
    }

    fun setApiKey(){
        val inputKey = (_apiKey.value as UiState.Loaded).data
        if (inputKey != ""){
            (ClientModel.getInstance().model.fetcher as ClientFetcher).apiKey = inputKey
        }else{
            (ClientModel.getInstance().model.fetcher as ClientFetcher).apiKey = null
        }
    }
}