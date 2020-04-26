package com.selvaraj.forecastweatherapp.model

sealed class UiState {
    object Loading : UiState()
    data class Success(val success: String) : UiState()
    data class Error(val message: String) : UiState()
}