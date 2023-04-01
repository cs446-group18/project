package com.cs446group18.delaywise.ui.flightinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.util.UiState
import com.cs446group18.lib.models.FlightInfo
import com.cs446group18.lib.models.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FlightInfoViewModel(private val flightIata: String) : ViewModel() {
    private val _flightState = MutableStateFlow<UiState<Pair<FlightInfo,List<FlightInfo>>>>(UiState.Loading())
    val flightState: StateFlow<UiState<Pair<FlightInfo,List<FlightInfo>>>> = _flightState

    val _isSaved = MutableStateFlow<Boolean>(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _flightState.emit(UiState.Loading())
            try {
                val flight = ClientModel.getInstance().getFlight(flightIata)
                val saveStatus = (ClientModel.getInstance().savedFlightDao.getItem(flightIata) != null)
                _flightState.value = UiState.Loaded(flight)
                _isSaved.value = saveStatus
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _flightState.value = UiState.Error(ex.toString())
            }
        }
    }

    suspend fun dateChangeTriggered(dateSelected: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            _flightState.emit(UiState.Loading())
            try {
                val flight = ClientModel.getInstance().getFlight(flightIata, dateSelected)
                val saveStatus = (ClientModel.getInstance().savedFlightDao.getItem(flightIata) != null)
                _flightState.value = UiState.Loaded(flight)
                _isSaved.value = saveStatus
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _flightState.value = UiState.Error(ex.toString())
            }
        }
    }

    suspend fun saveActionTriggered(id: String){
        try {
            val (flightInfo, _) = ClientModel.getInstance().getFlight(id)
            val jsonString = Json.encodeToString(flightInfo)
            val flightInfoEntity = SavedFlightEntity(flightInfo.ident_iata, jsonString)
            ClientModel.getInstance().savedFlightDao.insert(flightInfoEntity)
            _isSaved.value = true
        } catch (ex: Exception) {
            println(ex.toString())
            println(ex.stackTraceToString())
        }
    }

    suspend fun removeActionTriggered(id: String){
        try {
            val (flightInfo, _) = ClientModel.getInstance().getFlight(id)
            val jsonString = Json.encodeToString(flightInfo)
            val flightInfoEntity = SavedFlightEntity(flightInfo.ident_iata, jsonString)
            ClientModel.getInstance().savedFlightDao.delete(flightInfoEntity)
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
}
