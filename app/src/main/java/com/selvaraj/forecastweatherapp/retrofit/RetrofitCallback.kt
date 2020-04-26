package com.selvaraj.forecastweatherapp.retrofit

import retrofit2.Callback

interface RetrofitCallback<T> : Callback<T> {
    fun onNoNetwork()
}