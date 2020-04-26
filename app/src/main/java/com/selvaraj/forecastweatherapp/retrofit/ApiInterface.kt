package com.selvaraj.forecastweatherapp.retrofit

import com.selvaraj.forecastweatherapp.model.response.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("forecast")
    fun getWeatherData(
        @Query("appid") appid: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String
    ): Observable<WeatherResponse>
}