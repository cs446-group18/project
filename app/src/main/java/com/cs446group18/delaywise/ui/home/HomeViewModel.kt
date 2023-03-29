package com.cs446group18.delaywise.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.model.SavedFlightEntity
import com.cs446group18.delaywise.ui.components.DelayType
import com.cs446group18.delaywise.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val _homeSavedFlightState = MutableSharedFlow<UiState<List<SavedFlightEntity>>>()
    val homeSavedFlightState = _homeSavedFlightState.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _homeSavedFlightState.emit(UiState.Loading())
            try {
                val savedFlights = ClientModel.getInstance().savedFlightDao.listFlights()
                delay(1000) //Todo: remove delay
                _homeSavedFlightState.emit(
                    UiState.Loaded(savedFlights)
                )
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _homeSavedFlightState.emit(UiState.Error(ex.toString()))
            }
        }
    }
}
