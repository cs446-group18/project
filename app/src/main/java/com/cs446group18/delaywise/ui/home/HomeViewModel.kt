package com.cs446group18.delaywise.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.Airline
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.model.FlightInfoEntity
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.ui.components.ListItem
import com.cs446group18.delaywise.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val _homeSavedFlightState = MutableStateFlow<UiState<List<SavedFlightEntity>>>(UiState.Loading())
    val homeSavedFlightState: StateFlow<UiState<List<SavedFlightEntity>>> = _homeSavedFlightState

    val _airportResults = MutableStateFlow<List<String>>(emptyList())
    val airportResults:  StateFlow<List<String>> = _airportResults

    val _airlineResults = MutableStateFlow<List<Airline>>(emptyList())
    val airlineResults: StateFlow<List<Airline>> = _airlineResults

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _homeSavedFlightState.emit(UiState.Loading())
            try {
                val savedFlights = ClientModel.getInstance().savedFlightDao.listFlights()
                _homeSavedFlightState.value = UiState.Loaded(savedFlights)

                val airportMappings = ClientModel.getInstance().airportsByIata
                var processedAirportSearchResults = mutableListOf<String>()

                val airlineMappings = ClientModel.getInstance().airlinesByIata
                var processedAirlineSearchResults = mutableListOf<Airline>()

                for (airport in airportMappings) {
                    processedAirportSearchResults.add(airport.value.iata + "/" + airport.value.icao + " || " + airport.value.airport)
                }
                _airportResults.value = processedAirportSearchResults

                for (airline in airlineMappings) {
                    processedAirlineSearchResults.add(airline.value)
                }
                _airlineResults.value = processedAirlineSearchResults


            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _homeSavedFlightState.value = UiState.Error(ex.toString())
            }
        }
    }
}
