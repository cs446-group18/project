package com.cs446group18.delaywise.ui.flightinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs446group18.delaywise.ui.components.DelayType

class FlightInfoViewModel : ViewModel() {
    data class FlightInfo(
        val flightDelay: Int,
        val flightDuration: String,
        val flightNumber :String,
        val departAirport : String,
        val arrivalAirport : String,
        val departCity : String,
        val arrivalCity : String,
        val departTime: String,
        val arrivalTime: String,
        val departDate : String,
        val arrivalDate: String,

    )
    data class FlightGateInfo(
        val departTerminal : String,
        val departGate : String,
        val arrivalTerminal : String,
        val arrivalGate : String,
        )

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}