package com.cs446group18.delaywise.ui.airportinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.Model
import com.cs446group18.delaywise.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AirportInfoViewModel(private val airport: String) : ViewModel() {
    private val _airportDelay = MutableSharedFlow<UiState<List<Int>>>()
    val airportDelay = _airportDelay.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _airportDelay.emit(UiState.Loading())
            try {
                val flight = Model.getAirportDelay(airport)

                delay(1000) //Todo: remove delay
                _airportDelay.emit(
                    UiState.Loaded(flight)
                )
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _airportDelay.emit(UiState.Error(ex.toString()))
            }
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}
