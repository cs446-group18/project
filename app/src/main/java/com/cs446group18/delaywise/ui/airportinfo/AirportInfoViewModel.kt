package com.cs446group18.delaywise.ui.airportinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.model.SavedAirportEntity
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.Airport
import com.cs446group18.lib.models.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AirportInfoViewModel(private val airportCode: String) : ViewModel() {
    private val _airportState =
        MutableStateFlow<UiState<Pair<Airport, List<Int>>>>(UiState.Loading())
    val airportState: StateFlow<UiState<Pair<Airport, List<Int>>>> = _airportState
    private val _weatherState = MutableStateFlow<UiState<WeatherResponse>>(UiState.Loading())
    val weatherState: StateFlow<UiState<WeatherResponse>> = _weatherState

    val _isSaved = MutableStateFlow<Boolean>(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _airportState.emit(UiState.Loading())
            try {
                val airport = ClientModel.getInstance().getAirport(airportCode)
                val weatherInfo = ClientModel.getInstance().getWeather(airportCode)
                val delayStatus =
                    ClientModel.getInstance().getAirportDelay(airportCode).getAverageDelays()
                val saveStatus =
                    (ClientModel.getInstance().savedAirportDao.getItem(airportCode) != null)
                _airportState.value = UiState.Loaded(Pair(airport, delayStatus))
                _weatherState.value = UiState.Loaded(weatherInfo)
                _isSaved.value = saveStatus


            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _airportState.value = (UiState.Error(ex.toString()))
            }
        }
    }

    suspend fun saveActionTriggered() {
        try {
            val (airportInfo, _) = (_airportState.value as UiState.Loaded).data
            val jsonString = Json.encodeToString(airportInfo)
            val airportInfoEntity = SavedAirportEntity(airportInfo.code_iata, jsonString)
            ClientModel.getInstance().savedAirportDao.insert(airportInfoEntity)
            _isSaved.value = true
        } catch (ex: Exception) {
            println(ex.toString())
            println(ex.stackTraceToString())
        }
    }

    suspend fun removeActionTriggered() {
        try {
            ClientModel.getInstance().savedAirportDao.delete(airportCode)
            _isSaved.value = false
        } catch (ex: Exception) {
            println(ex.toString())
            println(ex.stackTraceToString())
        }
    }
}
