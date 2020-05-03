package com.selvaraj.forecastweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.databinding.ForecastWeatherItemBinding
import com.selvaraj.forecastweatherapp.model.response.WeatherList
import com.selvaraj.forecastweatherapp.ui.WeatherFragment
import com.selvaraj.forecastweatherapp.viewmodel.ForecastWeatherItemViewModel

/**
 * The adapter class which binds the forecast weather items
 */
class ForecastWeatherAdapter(
    private var weatherList: MutableList<WeatherList>,
    private var itemClickCallback: WeatherFragment.Callback
) :
    RecyclerView.Adapter<ForecastWeatherAdapter.ForecastWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastWeatherViewHolder {
        val itemBinding: ForecastWeatherItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.forecast_weather_item,
            parent,
            false
        )
        itemBinding.apply {
            callback = itemClickCallback
        }
        return ForecastWeatherViewHolder(binding = itemBinding)
    }

    override fun getItemCount(): Int = weatherList.size

    override fun onBindViewHolder(holder: ForecastWeatherViewHolder, position: Int) {
        holder.bindItem(weatherList[holder.adapterPosition])
    }

    /**
     * To update the data once fetched from API
     */
    fun setList(weatherList: MutableList<WeatherList>) {
        this.weatherList = weatherList
        notifyDataSetChanged()
    }

    inner class ForecastWeatherViewHolder(private val binding: ForecastWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.itemForecast) {
        fun bindItem(weather: WeatherList) {
            binding.forecastViewModel =
                ForecastWeatherItemViewModel(itemView.context, weatherItem = weather)
        }
    }
}