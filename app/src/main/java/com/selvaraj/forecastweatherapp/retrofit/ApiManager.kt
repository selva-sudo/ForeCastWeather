package com.selvaraj.forecastweatherapp.retrofit

import com.google.gson.GsonBuilder
import com.selvaraj.forecastweatherapp.base.WeatherApplication
import com.selvaraj.forecastweatherapp.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class ApiManager {
    companion object {
        private const val TIMEOUT = 30.toLong()
        private lateinit var retrofitClient: Retrofit

        /**
         * OkHttpClient
         */
        private val okHttpClient =
            OkHttpClient().newBuilder().readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor { chain -> chain.proceed(getRequest(chain.request())) }
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

        /**
         * Method which builds the Request header
         *
         * @param request   request sent by user
         * @return Request with the header loaded
         * @throws IOException possibility of throwing IOException so handled
         */
        @Throws(IOException::class)
        private fun getRequest(request: Request): Request {
            return request.newBuilder().build()
        }

        /**
         * @return reference of AuthorisedClient
         */
        fun getApiInterface(): ApiInterface {
            if (!::retrofitClient.isInitialized) {
                retrofitClient = okHttpClient.buildRetrofit()
            }
            return retrofitClient.create(ApiInterface::class.java)
        }

        /**
         * Method which builds the retrofit with baseUrl and Client sent
         *
         * @param this@buildRetrofit Client with request and header details
         * @return Retrofit
         */
        private fun OkHttpClient.buildRetrofit(): Retrofit {
            val gsonBuilder = GsonBuilder().setLenient()
            val gson = gsonBuilder.create()
            return Retrofit.Builder().baseUrl(BASE_URL).client(this)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }

    /**
     * Common Method to check the network connection before retrofit call and perform appropriate action
     *
     * @param call     represents the method which need to be called (present in TGApiInterface)
     * @param callback represents the which method to be called if there is network present or not present (callback result)
     * @param <T>      represents the common template which is used in this method, able to get calling class using <T> format
     */
    private fun <T> request(call: Call<T>, callback: RetrofitCallback<T>) {
        if (WeatherApplication.isNetworkConnected) {
            call.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }
}