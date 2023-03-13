package com.cs446group18.delaywise.ui.flightinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.Model
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.FlightInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class FlightInfoViewModel(private val flightIata: String) : ViewModel() {
    private val _flightState = MutableSharedFlow<UiState<FlightInfo>>()
    val flightState = _flightState.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _flightState.emit(UiState.Loading())
            try {
                val flight = Model.getFlight(flightIata)
//                val flight: FlightInfo
//                if (flightIata == "AC8836") {
//                    flight = FlightInfo(
//                        delay = 39,
//                        flightDuration = 60+47,
//                        flightNumber = "Air Canada (AC8836)",
//                        depAirportIata = "YYZ",
//                        arrAirportIata = "RDU",
//                        depCity = "Toronto",
//                        arrCity = "Raleigh",
//                        depScheduled = LocalDateTime.of(2023, 3, 13, 14, 5),
//                        depEstimated = LocalDateTime.of(2023, 3, 13, 14, 44),
//                        arrScheduled = LocalDateTime.of(2023,3,13,15,52),
//                        arrEstimated = LocalDateTime.of(2023,3,13,16,31),
//                        depTerminal = "1",
//                        depGate = "F95",
//                        arrTerminal = "2",
//                        arrGate = "C12",
//                        flightIata = "AC8836",
//                        airlineIata = "AC",
//                        airlineName = "Air Canada",
//                        arrAirportName = "Lester B Pearson Intl",
//                        depAirportName = "Raleigh Durham Intl",
//                    )
//                }
//                else {
//                    flight = Model.getFlight(flightIata)
//                }
                delay(1000) //Todo: remove delay
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
