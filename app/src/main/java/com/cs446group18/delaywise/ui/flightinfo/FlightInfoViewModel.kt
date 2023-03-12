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

class FlightInfoViewModel : ViewModel() {
    private val _flightState = MutableSharedFlow<UiState<FlightInfo>>()
    val flightState = _flightState.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _flightState.emit(UiState.Loading())
            try {
                val flight = Model.getFlight()
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

//    data class FlightInfo(
//        val flightIata: String,
//        val flightNumber: String,
//        val airlineIata: String,
//        val depAirportIata: String,
//        val depTerminal: String,
//        val arrAirportIata: String,
//        val arrTerminal: String,
//        val depCity: String,
//        val departTime: String,
//        val departDate: String,
//        val arrCity: String,
//        val arrivalTime: String,
//        val arrivalDate: String,
//        val arrAirportName: String,
//        val depAirportName: String,
//        val airlineName: String,
//        val delay: Int?,
//        val flightDuration: Int,
//        val depGate: String,
//        val arrGate: String,
//    )

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
