package com.selvaraj.forecastweatherapp.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.base.WeatherApplication
import com.selvaraj.forecastweatherapp.model.DayWeather
import com.selvaraj.forecastweatherapp.model.response.WeatherResponse
import com.selvaraj.forecastweatherapp.utils.*

class ForecastWeatherViewModel(weatherResponse: WeatherResponse) : ViewModel() {
    private var context: Context? = null
    var weatherState: ObservableField<String> = ObservableField("")
    var weatherDescription: ObservableField<String> = ObservableField("")
    var minTemp: ObservableField<String> = ObservableField("")
    var maxTemp: ObservableField<String> = ObservableField("")
    var humidity: ObservableField<String> = ObservableField("")
    var wind: ObservableField<String> = ObservableField("")
    var pressure: ObservableField<String> = ObservableField("")
    var imageUrl: ObservableField<String> = ObservableField("")
    var period: ObservableField<String> = ObservableField("")

    private val dayWeatherData: MutableLiveData<MutableList<DayWeather>> = MutableLiveData()
    fun getDayWeatherData(): LiveData<MutableList<DayWeather>> = dayWeatherData

    init {
        context = WeatherApplication.mInstance
        weatherResponse.list?.let {
            val dayWeather: MutableList<DayWeather> = mutableListOf()
            for (weather in it) {
                val date = getDate(weather.dtTxt)
                val hours = date.getHourFromDate()
                val minutes = date.getMinFromDate()
                val period = "$hours:$minutes"
                var url: String? = ""
                val weatherItem = weather.weather
                if (weatherItem?.isNotEmpty() == true) {
                    url = weatherItem[0].icon
                }
                dayWeather.add(
                    DayWeather(
                        period = period,
                        weather = weather.main?.temp,
                        url = url
                    )
                )
            }
            dayWeatherData.value = dayWeather
            it[0].let { weatherData ->
                period.set(weatherData.dtTxt.getDateWithDay())
                weatherData.weather?.let { weatherItem ->
                    if (weatherItem.isNotEmpty()) {
                        weatherState.set(weatherItem[0].main)
                        weatherDescription.set(weatherItem[0].description)
                        imageUrl.set(weatherItem[0].icon.getIconUrl())
                    }
                }
                weatherData.main?.let { main ->
                    minTemp.set(
                        context?.getString(
                            R.string.weather_celsius_format,
                            main.tempMin
                        )
                    )
                    maxTemp.set(
                        context?.getString(
                            R.string.weather_celsius_format,
                            main.tempMax
                        )
                    )
                    humidity.set(main.humidity.toString() + "%")
                    pressure.set(main.pressure.toString() + " hPa")
                }
                weatherData.wind?.let { windData ->
                    wind.set(windData.speed.toString() + " m/s")
                }
            }
        }
    }
}
