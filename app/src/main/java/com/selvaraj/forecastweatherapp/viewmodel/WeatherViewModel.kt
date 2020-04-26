package com.selvaraj.forecastweatherapp.viewmodel

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.base.BaseViewModel
import com.selvaraj.forecastweatherapp.base.WeatherApplication
import com.selvaraj.forecastweatherapp.model.TodayWeather
import com.selvaraj.forecastweatherapp.model.UiState
import com.selvaraj.forecastweatherapp.model.response.WeatherList
import com.selvaraj.forecastweatherapp.model.response.WeatherResponse
import com.selvaraj.forecastweatherapp.retrofit.ApiInterface
import com.selvaraj.forecastweatherapp.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch

class WeatherViewModel : BaseViewModel() {
    private var context: Context? = null
    var progress: ObservableInt = ObservableInt(View.VISIBLE)
    var isFailed: ObservableInt = ObservableInt(View.GONE)
    var hasData: ObservableInt = ObservableInt(View.GONE)
    var todayDate: ObservableField<String> = ObservableField("")
    var todayTemp: ObservableField<String> = ObservableField("")
    var weatherStatus: ObservableField<String> = ObservableField("")
    var imageUrl: ObservableField<String> = ObservableField("")
    var humidity: ObservableField<String> = ObservableField("")
    var wind: ObservableField<String> = ObservableField("")
    var pressure: ObservableField<String> = ObservableField("")

    private val retryData: MutableLiveData<Any> = MutableLiveData()
    fun retryData(): LiveData<Any> = retryData

    private val todayWeatherData: MutableLiveData<MutableList<TodayWeather>> = MutableLiveData()
    fun getTodayWeatherData(): LiveData<MutableList<TodayWeather>> = todayWeatherData

    private val forecastWeatherData: MutableLiveData<MutableList<WeatherList>> = MutableLiveData()
    fun getForecastWeatherData(): LiveData<MutableList<WeatherList>> = forecastWeatherData

    private val compositeDisposable = CompositeDisposable()

    init {
        context = WeatherApplication.mInstance
    }

    fun onRetryClick(view: View) {
        progress.set(View.VISIBLE)
        retryData.value = ""
    }

    fun getWeatherData(latitude: Double, longitude: Double) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val application = WeatherApplication.mInstance
                val apiInterface: ApiInterface = application.mApiInterface
                val disposable: Disposable =
                    apiInterface.getWeatherData(
                        appid = APP_ID,
                        latitude = latitude,
                        longitude = longitude,
                        units = WEATHER_UNITS
                    )
                        .subscribeOn(application.subscribeScheduler())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response ->
                            updateUi(response)
                            progress.set(View.GONE)
                            isFailed.set(View.GONE)
                            hasData.set(View.VISIBLE)
                        }, { throwable ->
                            progress.set(View.GONE)
                            isFailed.set(View.VISIBLE)
                            hasData.set(View.GONE)
                            throwable.printStackTrace()
                        })

                compositeDisposable.add(disposable)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    private fun updateUi(response: WeatherResponse?) {
        val list = response?.list
        if (list?.isNotEmpty() == true) {
            val weatherItem = list[0]
            todayDate.set(weatherItem.dtTxt.getDateWithDay())

            todayTemp.set(
                context?.getString(
                    R.string.weather_celsius_format,
                    weatherItem.main?.temp
                )
            )
            humidity.set(weatherItem.main?.humidity.toString() + "%")
            wind.set(weatherItem.wind?.speed.toString() + " m/s")
            pressure.set(weatherItem.main?.pressure.toString() + " hPa")
            val weather = weatherItem.weather
            if (weather?.isNotEmpty() == true) {
                val weatherDetails = weather[0]
                weatherStatus.set(weatherDetails.main)
                imageUrl.set(weatherDetails.icon.getIconUrl())
            }

            val weatherItemList = getWeatherItemList(list)
            todayWeatherData.value = weatherItemList.first
            forecastWeatherData.value = weatherItemList.second
        }
    }
}