package com.selvaraj.forecastweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.databinding.TodayWeatherItemBinding
import com.selvaraj.forecastweatherapp.model.DayWeather
import com.selvaraj.forecastweatherapp.viewmodel.TodayWeatherItemViewModel

/**
 * The adapter class which binds the data of each day weather.
 */
class DayWeatherAdapter(private var dayWeather: MutableList<DayWeather>) :
    RecyclerView.Adapter<DayWeatherAdapter.TodayWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayWeatherViewHolder {
        val itemBinding: TodayWeatherItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.today_weather_item,
            parent,
            false
        )
        return TodayWeatherViewHolder(binding = itemBinding)
    }

    override fun getItemCount(): Int = dayWeather.size

    override fun onBindViewHolder(holder: TodayWeatherViewHolder, position: Int) {
        holder.bindItem(dayWeather[holder.adapterPosition])
    }

    fun setList(dayWeather: MutableList<DayWeather>) {
        this.dayWeather = dayWeather
        notifyDataSetChanged()
    }

    inner class TodayWeatherViewHolder(private val binding: TodayWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.itemToday) {
        fun bindItem(weather: DayWeather) {
            binding.todayViewModel =
                TodayWeatherItemViewModel(
                    itemView.context,
                    dayItem = weather,
                    position = dayWeather.indexOf(weather)
                )
        }
    }
}