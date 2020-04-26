package com.selvaraj.forecastweatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.selvaraj.forecastweatherapp.adapter.DayWeatherAdapter
import com.selvaraj.forecastweatherapp.adapter.ForecastWeatherAdapter
import com.selvaraj.forecastweatherapp.databinding.FragmentWeatherBinding
import com.selvaraj.forecastweatherapp.model.DayWeather
import com.selvaraj.forecastweatherapp.model.LocationGotEvent
import com.selvaraj.forecastweatherapp.model.response.WeatherList
import com.selvaraj.forecastweatherapp.ui.WeatherFragmentDirections.actionForecastWeatherFragment
import com.selvaraj.forecastweatherapp.utils.LocationServicesManager
import com.selvaraj.forecastweatherapp.utils.getParticularWeatherList
import com.selvaraj.forecastweatherapp.utils.toast
import com.selvaraj.forecastweatherapp.viewmodel.WeatherViewModel
import com.selvaraj.forecastweatherapp.viewmodel.WeatherViewModelFactory
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*

@RuntimePermissions
class WeatherFragment : Fragment() {

    private var dayAdapter: DayWeatherAdapter? = null
    private var forecastAdapter: ForecastWeatherAdapter? = null

    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, WeatherViewModelFactory)
            .get(WeatherViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Fade(Fade.OUT).apply {
            duration = LARGE_EXPAND_DURATION / 2
            interpolator = FAST_OUT_LINEAR_IN
        }
        reenterTransition = Fade(Fade.IN).apply {
            duration = LARGE_COLLAPSE_DURATION / 2
            startDelay = LARGE_COLLAPSE_DURATION / 2
            interpolator = LINEAR_OUT_SLOW_IN
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWeatherBinding.inflate(inflater, container, false).apply {
            viewmodel = weatherViewModel
            lifecycleOwner = viewLifecycleOwner
            setTodayAdapters(rvTodayWeather)
            setForecastAdapter(rvForecastWeather)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWeatherDetailsWithPermissionCheck()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        weatherViewModel.retryData().observe(requireActivity(), Observer {
            getWeatherDetailsWithPermissionCheck()
        })

        weatherViewModel.getTodayWeatherData().observe(requireActivity(), Observer { list ->
            dayAdapter?.setList(list)
        })

        weatherViewModel.getForecastWeatherData().observe(requireActivity(), Observer { list ->
            forecastAdapter?.setList(list)
        })
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherViewModel.reset()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationEvent(event: LocationGotEvent) {
        event.location?.let {
            val latitude = it.latitude
            val longitude = it.longitude
            weatherViewModel.getWeatherData(latitude, longitude)
        }
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getWeatherDetails() {
        LocationServicesManager.getInstance(requireContext())?.requestLocation(Looper.myLooper())
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getWeatherDataNeverAskAgain() {
        requireContext().toast("Allow forecast weather to access your device location. Tap Setting > Permissions, and turn on required permissions")
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(permissionRequest: PermissionRequest) {
        permissionRequest.proceed()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationDenied() {
        requireContext().toast("Location permission needed")
    }

    private fun setTodayAdapters(rvTodayWeather: RecyclerView) {
        val dayItems: MutableList<DayWeather> = mutableListOf()
        dayAdapter = DayWeatherAdapter(dayItems)
        rvTodayWeather.adapter = dayAdapter
        rvTodayWeather.setHasFixedSize(true)
    }

    private fun setForecastAdapter(rvForecastWeather: RecyclerView) {
        val forecastItems: MutableList<WeatherList> = mutableListOf()
        forecastAdapter = ForecastWeatherAdapter(
            weatherList = forecastItems,
            itemClickCallback = object : Callback {
                override fun onForecastItemClick(weatherItem: WeatherList?) {

                }
            })
        rvForecastWeather.adapter = forecastAdapter
        rvForecastWeather.setHasFixedSize(true)
    }

    interface Callback {
        fun onForecastItemClick(weatherItem: WeatherList?)
    }
}
