package com.selvaraj.forecastweatherapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    @Expose
    val lat: Double? = null,

    @SerializedName("lon")
    @Expose
    val lon: Double? = null
)