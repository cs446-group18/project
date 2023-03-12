package com.cs446group18.delaywise.ui.flightinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.Model
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.FlightInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FlightInfoViewModel(private val flightIata: String) : ViewModel() {
    private val _flightState = MutableSharedFlow<UiState<FlightInfo>>()
    val flightState = _flightState.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _flightState.emit(UiState.Loading())
            try {
                val flight = Model.getFlight(flightIata)
                _flightState.emit(
                    UiState.Loaded(flight)
                )
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _flightState.emit(UiState.Error(ex.toString()))
            }
        }
    }

    data class FlightGateInfo(
        val departTerminal: String,
        val departGate: String,
        val arrivalTerminal: String,
        val arrivalGate: String,
    )

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}
