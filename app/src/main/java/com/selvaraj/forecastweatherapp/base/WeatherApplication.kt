package com.selvaraj.forecastweatherapp.base

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import com.selvaraj.forecastweatherapp.retrofit.ApiInterface
import com.selvaraj.forecastweatherapp.retrofit.ApiManager
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Base application class.
 */
class WeatherApplication : Application() {
    private val TAG = WeatherApplication::class.java.simpleName
    lateinit var mApiInterface: ApiInterface
    private var scheduler: Scheduler? = null

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mApiInterface = ApiManager.getApiInterface()
        startNetworkListener()
    }

    /**
     * To subscribe the scheduler for disposable
     */
    fun subscribeScheduler(): Scheduler? {
        if (scheduler == null) {
            scheduler = Schedulers.io()
        }
        return scheduler
    }

    companion object {
        var isNetworkConnected: Boolean = false
        lateinit var mInstance: WeatherApplication

        /**
         * Method to check network connectivity status
         */
        fun startNetworkListener() {
            try {
                val connectivityManager =
                    mInstance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val builder = NetworkRequest.Builder()
                connectivityManager.registerNetworkCallback(builder.build(),
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network?) {
                            isNetworkConnected = true
                        }

                        override fun onLost(network: Network?) {
                            isNetworkConnected = false
                        }
                    })
            } catch (exception: Exception) {
                Log.d(mInstance.TAG, "Got exception while getting network state")
            }
        }
    }
}