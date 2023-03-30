package com.cs446group18.delaywise.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs446group18.delaywise.model.ClientModel
import com.cs446group18.delaywise.model.SavedFlightEntity
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _homeSavedFlightState.emit(UiState.Loading())
            try {
                val savedFlights = ClientModel.getInstance().savedFlightDao.listFlights()
                _homeSavedFlightState.value = UiState.Loaded(savedFlights)
            } catch (ex: Exception) {
                println(ex.toString())
                println(ex.stackTraceToString())
                _homeSavedFlightState.value = UiState.Error(ex.toString())
            }
        }
    }
}
