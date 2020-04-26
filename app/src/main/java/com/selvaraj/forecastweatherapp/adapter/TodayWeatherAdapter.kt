package com.selvaraj.forecastweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.selvaraj.forecastweatherapp.R
import com.selvaraj.forecastweatherapp.databinding.TodayWeatherItemBinding
import com.selvaraj.forecastweatherapp.model.TodayWeather
import com.selvaraj.forecastweatherapp.viewmodel.TodayWeatherItemViewModel

class TodayWeatherAdapter(private var todayWeather: MutableList<TodayWeather>) :
    RecyclerView.Adapter<TodayWeatherAdapter.TodayWeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayWeatherViewHolder {
        val itemBinding: TodayWeatherItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.today_weather_item,
            parent,
            false
        )
        return TodayWeatherViewHolder(binding = itemBinding)
    }

    override fun getItemCount(): Int = todayWeather.size

    override fun onBindViewHolder(holder: TodayWeatherViewHolder, position: Int) {
        holder.bindItem(todayWeather[holder.adapterPosition])
    }

    fun setList(todayWeather: MutableList<TodayWeather>) {
        this.todayWeather = todayWeather
        notifyDataSetChanged()
    }

    inner class TodayWeatherViewHolder(private val binding: TodayWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.itemToday) {
        fun bindItem(todayWeather: TodayWeather) {
            binding.todayViewModel =
                TodayWeatherItemViewModel(itemView.context, todayItem = todayWeather)
        }
    }
}