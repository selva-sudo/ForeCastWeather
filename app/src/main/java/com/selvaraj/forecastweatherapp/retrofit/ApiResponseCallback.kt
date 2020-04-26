package com.selvaraj.forecastweatherapp.retrofit

import com.selvaraj.forecastweatherapp.model.response.WeatherResponse

interface ApiResponseCallback {
    fun onSuccess(response: WeatherResponse)
    fun onFailure(errorMessage: String)
    fun onNoNetwork()
}