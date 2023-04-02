package com.cs446group18.delaywise.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.*
import com.cs446group18.delaywise.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val _homeSavedState =
        MutableStateFlow<UiState<Pair<List<SavedFlightEntity>, List<SavedAirportEntity>>>>(UiState.Loading())
    val homeSavedState: StateFlow<UiState<Pair<List<SavedFlightEntity>, List<SavedAirportEntity>>>> =
        _homeSavedState

    val _airportResults = MutableStateFlow<List<Airport>>(emptyList())
    val airportResults: StateFlow<List<Airport>> = _airportResults

    val _airlineResults = MutableStateFlow<List<Airline>>(emptyList())
    val airlineResults: StateFlow<List<Airline>> = _airlineResults

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _homeSavedState.emit(UiState.Loading())
            try {
                val savedFlights = ClientModel.getInstance().savedFlightDao.listFlights()
                val savedAirports = ClientModel.getInstance().savedAirportDao.listAirports()
                _homeSavedState.value = UiState.Loaded(Pair(savedFlights, savedAirports))

                val airportMappings = ClientModel.getInstance().airportsByIata
                var processedAirportSearchResults = mutableListOf<Airport>()

                val airlineMappings = ClientModel.getInstance().airlinesByIata
                var processedAirlineSearchResults = mutableListOf<Airline>()

                for (airport in airportMappings) {
                    processedAirportSearchResults.add(airport.value)
                }
                _airportResults.value = processedAirportSearchResults

                for (airline in airlineMappings) {
                    processedAirlineSearchResults.add(airline.value)
                }
                _airlineResults.value = processedAirlineSearchResults

            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _homeSavedState.value = UiState.Error(ex.toString())
            }
        }
    }
}
