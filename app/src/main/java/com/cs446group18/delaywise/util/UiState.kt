package com.cs446group18.delaywise.util

sealed class UiState<T> {
    class Empty<T> : UiState<T>()
    class Loading<T> : UiState<T>()
    class Loaded<T>(val data: T) : UiState<T>()
    class Error<T>(val message: String) : UiState<T>()
}
