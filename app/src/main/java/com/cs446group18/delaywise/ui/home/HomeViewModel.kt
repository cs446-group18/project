package com.cs446group18.delaywise.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs446group18.delaywise.ui.components.DelayType

class HomeViewModel : ViewModel() {
    data class Flight(
        val flightNumber : String,
        val delayText : String,
        val delayType: DelayType,
        val departAirport: String,
        val arrivalAirport: String,
        val dateDepart: String
    )
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}
