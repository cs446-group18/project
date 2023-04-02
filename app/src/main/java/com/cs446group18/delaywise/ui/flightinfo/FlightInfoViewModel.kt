package com.cs446group18.delaywise.ui.flightinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.FlightInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class FlightInfoViewModel(private val flightIata: String, private val date: LocalDate? = null) :
    ViewModel() {
    private val _flightState =
        MutableStateFlow<UiState<Pair<FlightInfo, List<FlightInfo>>>>(UiState.Loading())
    val flightState: StateFlow<UiState<Pair<FlightInfo, List<FlightInfo>>>> = _flightState

    val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _flightState.emit(UiState.Loading())
            try {
                val flightInfo = ClientModel.getInstance().getFlight(flightIata, date)

                _flightState.value = UiState.Loaded(flightInfo)
                _isSaved.value =
                    ClientModel.getInstance().savedFlightDao.getItem(flightInfo.first) != null
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _flightState.value = UiState.Error(ex.toString())
            }
        }
    }

    suspend fun dateChangeTriggered(date: LocalDate) {
        try {
            val flightList = (_flightState.value as UiState.Loaded).data.second
            val flight = flightList.find { it.getDepartureDate() == date }!!
            val saveStatus = (ClientModel.getInstance().savedFlightDao.getItem(flight) != null)
            _flightState.value = UiState.Loaded(Pair(flight, flightList))
            _isSaved.value = saveStatus
        } catch (ex: Exception) {
            println(ex.toString())
            println(ex.stackTraceToString())
            _flightState.value = UiState.Error(ex.toString())
        }
    }

    suspend fun saveActionTriggered() {
        try {
            val (flight, _) = (_flightState.value as UiState.Loaded).data
            ClientModel.getInstance().savedFlightDao.insert(flight)
            _isSaved.value = true
        } catch (ex: Exception) {
            println(ex.toString())
            println(ex.stackTraceToString())
        }
    }

    suspend fun removeActionTriggered() {
        try {
            val (flight, _) = (_flightState.value as UiState.Loaded).data
            ClientModel.getInstance().savedFlightDao.delete(flight)
            _isSaved.value = false
        } catch (ex: Exception) {
            println(ex.toString())
            println(ex.stackTraceToString())
        }
    }

    data class FlightGateInfo(
        val departTerminal: String,
        val departGate: String,
        val arrivalTerminal: String,
        val arrivalGate: String,
    )

    data class WeatherData(
        val date: String,
        val weather: String,
    )

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}
