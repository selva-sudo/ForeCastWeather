package com.selvaraj.forecastweatherapp.utils

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.*
import com.selvaraj.forecastweatherapp.model.LocationGotEvent
import org.greenrobot.eventbus.EventBus

/**
 * The manager class for location services
 */
class LocationServicesManager() {
    constructor(context: Context) : this() {
        this.context = context
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //initialise the fused location client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    companion object {
        private var instance: LocationServicesManager? = null

        @Synchronized
        fun getInstance(context: Context): LocationServicesManager? {
            if (instance == null) {
                instance = LocationServicesManager(context)
            }
            return instance
        }
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var context: Context? = null
    private var locationManager: LocationManager? = null
    private var lastLocation: Location? = null

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            if (locationResult != null) {
                lastLocation = locationResult.lastLocation
            }
            EventBus.getDefault().post(LocationGotEvent(lastLocation))
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            super.onLocationAvailability(locationAvailability)
            if (!locationAvailability.isLocationAvailable) {
                EventBus.getDefault().post(LocationGotEvent(null))
            }
        }
    }

    /**
     * To request the location updates
     */
    fun requestLocation(mainLooper: Looper?) {
        mFusedLocationClient?.requestLocationUpdates(
            getLocationRequestObject(),
            locationCallback,
            mainLooper
        )
    }

    /**
     * Method to get the location request object which contains the distance and time to update when in foreground
     *
     * @return location request object containing the required time and distance
     */
    private fun getLocationRequestObject(): LocationRequest? {
        val locationRequest = LocationRequest()
        locationRequest.interval = 120000
        locationRequest.fastestInterval = 120000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return locationRequest
    }

    /**
     * To remove the location updates
     */
    fun removeLocationUpdates() {
        mFusedLocationClient?.removeLocationUpdates(locationCallback)
    }
}