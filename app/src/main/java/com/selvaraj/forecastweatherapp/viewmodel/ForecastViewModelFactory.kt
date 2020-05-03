package com.selvaraj.forecastweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.selvaraj.forecastweatherapp.model.response.WeatherResponse

/**
 * The view model factory which instantiate [ForecastWeatherViewModel]
 */
class ForecastViewModelFactory(private val weatherResponse: WeatherResponse) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastWeatherViewModel::class.java)) {
            return ForecastWeatherViewModel(weatherResponse = weatherResponse) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}