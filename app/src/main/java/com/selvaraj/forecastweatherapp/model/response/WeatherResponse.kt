package com.selvaraj.forecastweatherapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherResponse {
    @SerializedName("cod")
    @Expose
    val cod: String? = null

    @SerializedName("message")
    @Expose
    val message: Int? = null

    @SerializedName("cnt")
    @Expose
    val cnt: Int? = null

    @SerializedName("list")
    @Expose
    val list: List<WeatherList>? = null

    @SerializedName("city")
    @Expose
    val city: City? = null
}