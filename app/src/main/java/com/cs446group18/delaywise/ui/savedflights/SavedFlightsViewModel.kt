package com.cs446group18.delaywise.ui.flightinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.model.SavedAirportEntity
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedFlightsViewModel(): ViewModel() {
    private val _savedFlightAirportState = MutableStateFlow<UiState<Pair<List<SavedFlightEntity>,List<SavedAirportEntity>>>>(UiState.Loading())
    val savedFlightAirportState: StateFlow<UiState<Pair<List<SavedFlightEntity>,List<SavedAirportEntity>>>> = _savedFlightAirportState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _savedFlightAirportState.emit(UiState.Loading())
            try {
                val savedFlights = ClientModel.getInstance().savedFlightDao.listFlights()
                val savedAirports = ClientModel.getInstance().savedAirportDao.listAirports()
                _savedFlightAirportState.value = UiState.Loaded(Pair(savedFlights, savedAirports))
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _savedFlightAirportState.value = UiState.Error(ex.toString())
            }
        }
    }
}
