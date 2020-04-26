package com.selvaraj.forecastweatherapp.utils

import android.animation.TimeInterpolator
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.core.view.animation.PathInterpolatorCompat
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.base.WeatherApplication
import com.selvaraj.forecastweatherapp.model.DayWeather
import com.selvaraj.forecastweatherapp.model.response.WeatherList
import com.selvaraj.forecastweatherapp.model.response.WeatherResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val WEATHER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun Date?.getDayFromDate() = DateFormat.format("EEEE", this) as String // Thursday

fun Date?.getDateFromDate() = DateFormat.format("dd", this) as String // 20

fun Date?.getHourFromDate() = DateFormat.format("HH", this) as String // 10

fun Date?.getMinFromDate() = DateFormat.format("mm", this) as String // :30

fun String?.getDateWithDay(): String {
    val date = getDate(this)
    return date.getDayFromDate() + "," + date.getDateFromDate()
}

fun String?.getIconUrl(): String = WEATHER_ICON_URL + this + WEATHER_ITEM_EXTENSION

fun getDate(dateResponse: String?): Date? {
    val format = SimpleDateFormat(WEATHER_DATE_PATTERN, Locale.getDefault())
    try {
        return format.parse(dateResponse ?: "")
    } catch (e: ParseException) {
        Log.d("DateFormat", "Exception while parsing date")
    }
    return null
}

fun checkToday(date: Date?): Boolean {
    return Date().getDateFromDate() == date.getDateFromDate()
}

fun Date?.checkDatesNotEqual(date: Date?): Boolean =
    this.getDateFromDate() != date.getDateFromDate()

fun Date?.checkDatesEqual(date: Date?): Boolean =
    this.getDateFromDate() == date.getDateFromDate()


/**
 * Accelerate easing.
 *
 * Elements exiting a screen use acceleration easing, where they start at rest and end at peak
 * velocity.
 */
val FAST_OUT_LINEAR_IN: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
    PathInterpolatorCompat.create(0.4f, 0f, 1f, 1f)
}


/**
 * Decelerate easing.
 *
 * Incoming elements are animated using deceleration easing, which starts a transition at peak
 * velocity (the fastest point of an element’s movement) and ends at rest.
 */
val LINEAR_OUT_SLOW_IN: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
    PathInterpolatorCompat.create(0f, 0f, 0.2f, 1f)
}

fun getWeatherItemList(weatherList: List<WeatherList>): Pair<MutableList<TodayWeather>, MutableList<WeatherList>> {
    val todayWeather: MutableList<TodayWeather> = mutableListOf()
    val forecastWeather: MutableList<WeatherList> = mutableListOf()
    var dateForCheck: Date? = Date()
    for (position in weatherList.indices) {
        val weather = weatherList[position]
        val weatherItem = weather.weather
        val date = getDate(weather.dtTxt)
        if (checkToday(date)) {
            var url: String? = ""
            if (weatherItem?.isNotEmpty() == true) {
                url = weatherItem[0].icon
            }
            val hours = date.getHourFromDate()
            val minutes = date.getMinFromDate()
            val period =
                if (position == 0) WeatherApplication.mInstance.getString(R.string.now) else "$hours:$minutes"
            dayWeather.add(
                DayWeather(
                    period = period,
                    weather = weather.main?.temp,
                    url = url
                )
            )
        } else if (date.checkDatesNotEqual(dateForCheck)) {
            forecastWeather.add(weather)
            dateForCheck = date
        }
    }
    return Pair(dayWeather, forecastWeather)
}

fun getParticularWeatherList(
    weatherItem: WeatherList,
    weatherList: List<WeatherList>?
): WeatherResponse {
    val selectedWeatherList: MutableList<WeatherList> = mutableListOf()
    val dateForCheck = getDate(weatherItem.dtTxt)
    weatherList?.let {
        for (weather in weatherList) {
            val date = getDate(weather.dtTxt)
            if (date.checkDatesEqual(dateForCheck)) {
                selectedWeatherList.add(weather)
            } else if (selectedWeatherList.isNotEmpty()) {
                break
            }
        }
    }
    return WeatherResponse().apply {
        list = selectedWeatherList
    }
}