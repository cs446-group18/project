package com.cs446group18.delaywise.ui.flightinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.FlightInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SavedFlightsViewModel(): ViewModel() {
    private val _savedFlightState = MutableSharedFlow<UiState<List<SavedFlightEntity>>>()
    val savedFlightState = _savedFlightState.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _savedFlightState.emit(UiState.Loading())
            try {
                val savedFlights = ClientModel.getInstance().savedFlightDao.listFlights()
                delay(1000) //Todo: remove delay
                _savedFlightState.emit(
                    UiState.Loaded(savedFlights)
                )
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _savedFlightState.emit(UiState.Error(ex.toString()))
            }
        }
    }
}
