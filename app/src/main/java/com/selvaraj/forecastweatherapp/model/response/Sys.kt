package com.selvaraj.forecastweatherapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    @Expose
    private val pod: String? = null
)