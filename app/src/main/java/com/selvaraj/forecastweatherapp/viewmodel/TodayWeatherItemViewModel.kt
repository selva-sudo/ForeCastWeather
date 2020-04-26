package com.selvaraj.forecastweatherapp.viewmodel

import android.content.Context
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.base.BaseObservable
import com.selvaraj.forecastweatherapp.model.TodayWeather
import com.selvaraj.forecastweatherapp.utils.getIconUrl

class TodayWeatherItemViewModel(context: Context, var todayItem: TodayWeather) :
    BaseObservable() {
    val period = todayItem.period
    val weather = context.getString(R.string.weather_celsius_format, todayItem.weather)
    val imageUrl = todayItem.url.getIconUrl()
}