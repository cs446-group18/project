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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SavedFlightsViewModel(): ViewModel() {
    private val _savedFlightState = MutableStateFlow<UiState<List<SavedFlightEntity>>>(UiState.Loading())
    val savedFlightState: StateFlow<UiState<List<SavedFlightEntity>>> = _savedFlightState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _savedFlightState.emit(UiState.Loading())
            try {
                val savedFlights = ClientModel.getInstance().savedFlightDao.listFlights()
                _savedFlightState.value = UiState.Loaded(savedFlights)
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _savedFlightState.value = UiState.Error(ex.toString())
            }
        }
    }
}
