package com.selvaraj.forecastweatherapp.viewmodel

import android.content.Context
import android.view.View
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.base.BaseObservable
import com.selvaraj.forecastweatherapp.model.DayWeather
import com.selvaraj.forecastweatherapp.utils.getIconUrl

class TodayWeatherItemViewModel(
    context: Context,
    var dayItem: DayWeather,
    position: Int
) :
    BaseObservable() {
    val period = dayItem.period
    val weather = context.getString(R.string.weather_celsius_format, dayItem.weather)
    val imageUrl = dayItem.url.getIconUrl()
    val showVerticalLine = if (position == 0) View.GONE else View.VISIBLE
}