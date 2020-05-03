package com.selvaraj.forecastweatherapp.viewmodel

import android.content.Context
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.base.BaseObservable
import com.selvaraj.forecastweatherapp.model.response.WeatherList
import com.selvaraj.forecastweatherapp.utils.getDateWithDay
import com.selvaraj.forecastweatherapp.utils.getIconUrl

/**
 * The viewmodel class for forecast weather item
 */
class ForecastWeatherItemViewModel(context: Context, var weatherItem: WeatherList) :
    BaseObservable() {
    val period = weatherItem.dtTxt.getDateWithDay()
    val minWeather = context.getString(R.string.weather_celsius_format, weatherItem.main?.tempMin)
    val weatherStatus = weatherItem.weather?.get(0)?.main
    val imageUrl = weatherItem.weather?.get(0)?.icon.getIconUrl()
}