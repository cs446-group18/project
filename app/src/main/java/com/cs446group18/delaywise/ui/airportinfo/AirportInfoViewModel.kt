package com.cs446group18.delaywise.ui.airportinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.model.SavedAirportEntity
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AirportInfoViewModel(private val airport: String) : ViewModel() {
    private val _airportDelay = MutableSharedFlow<UiState<List<Int>>>()
    val airportDelay = _airportDelay.asSharedFlow()

    val _isSaved = MutableSharedFlow<Boolean>()
    val isSaved = _isSaved.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _airportDelay.emit(UiState.Loading())
            try {
                val flight = ClientModel.getInstance().getAirportDelay(airport).getAverageDelays()
                _isSaved.emit(ClientModel.getInstance().savedAirportDao.getItem(airport) == null)
                _airportDelay.emit(
                    UiState.Loaded(flight)
                )
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _airportDelay.emit(UiState.Error(ex.toString()))
            }
        }
    }
    suspend fun saveActionTriggered(id: String){
        try {
            val airportInfo = ClientModel.getInstance().getAirport(id)
            val jsonString = Json.encodeToString(airportInfo)
            val airportInfoEntity = SavedAirportEntity(airportInfo.code_iata, jsonString)
            ClientModel.getInstance().savedAirportDao.insert(airportInfoEntity)
            _isSaved.emit(true)
        } catch (ex: Exception) {
            println(ex.toString())
            println(ex.stackTraceToString())
        }
    }

    suspend fun removeActionTriggered(id: String){
        try {
            val airportInfo = ClientModel.getInstance().getAirport(id)
            val jsonString = Json.encodeToString(airportInfo)
            val airportInfoEntity = SavedAirportEntity(airportInfo.code_iata, jsonString)
            ClientModel.getInstance().savedAirportDao.delete(airportInfoEntity)
            _isSaved.emit(false)
        } catch (ex: Exception) {
            println(ex.toString())
            println(ex.stackTraceToString())
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}
