package com.selvaraj.forecastweatherapp.utils

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.base.WeatherApplication
import com.selvaraj.forecastweatherapp.model.DayWeather
import com.selvaraj.forecastweatherapp.model.response.WeatherList
import com.selvaraj.forecastweatherapp.model.response.WeatherResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * To display the toast message with [Context]
 */
fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }

/**
 * To get the day of the month from [Date]
 */
fun Date?.getDayFromDate() = DateFormat.format("EEEE", this) as String // Thursday

/**
 * To get the date of the month from [Date]
 */
fun Date?.getDateFromDate() = DateFormat.format("dd", this) as String // 20

/**
 * To get the hour of the day from [Date]
 */
fun Date?.getHourFromDate() = DateFormat.format("HH", this) as String // 10

/**
 * To get the minute
 */
fun Date?.getMinFromDate() = DateFormat.format("mm", this) as String // :30

/**
 * To get the date appended with date string
 */
fun String?.getDateWithDay(): String {
    val date = getDate(this)
    return date.getDayFromDate() + "," + date.getDateFromDate()
}

/**
 * To get the icon URL to fetch the weather icon.
 */
fun String?.getIconUrl(): String = WEATHER_ICON_URL + this + WEATHER_ITEM_EXTENSION

/**
 * To get the date from the specified [SimpleDateFormat]
 */
fun getDate(dateResponse: String?): Date? {
    val format = SimpleDateFormat(WEATHER_DATE_PATTERN, Locale.getDefault())
    try {
        return format.parse(dateResponse ?: "")
    } catch (e: ParseException) {
        Log.d("DateFormat", "Exception while parsing date")
    }
    return null
}

/**
 * To check whether the date is today or not
 */
fun Date?.checkToday() = Date().getDateFromDate() == this.getDateFromDate()

/**
 * To check the two dates are not equal
 */
fun Date?.checkDatesNotEqual(date: Date?): Boolean =
    this.getDateFromDate() != date.getDateFromDate()

/**
 * To check the dates are equal
 */
fun Date?.checkDatesEqual(date: Date?): Boolean =
    this.getDateFromDate() == date.getDateFromDate()

/**
 * To get the weather items list to display in main screen
 * Get the filtered Today weather data and Forecast weather data (For next five days(First hour of the day))
 * @return The [Pair] data.
 */
fun getWeatherItemList(weatherList: List<WeatherList>): Pair<MutableList<DayWeather>, MutableList<WeatherList>> {
    val dayWeather: MutableList<DayWeather> = mutableListOf()
    val forecastWeather: MutableList<WeatherList> = mutableListOf()
    var dateForCheck: Date? = Date()
    for (position in weatherList.indices) {
        val weather = weatherList[position]
        val weatherItem = weather.weather
        val date = getDate(weather.dtTxt)
        if (date.checkToday()) {
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

/**
 * To get the particular days weather data from the whole weather response.
 */
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