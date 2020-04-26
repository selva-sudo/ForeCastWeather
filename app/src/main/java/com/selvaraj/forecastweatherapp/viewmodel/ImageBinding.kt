package com.selvaraj.forecastweatherapp.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.selvaraj.forecastweatherapp.R

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url)
        .placeholder(R.drawable.ic_cloud_sun)
        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
        .into(imageView)
}