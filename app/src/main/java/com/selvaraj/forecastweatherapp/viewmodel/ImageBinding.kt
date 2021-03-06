package com.selvaraj.forecastweatherapp.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.selvaraj.forecastweatherapp.R

/**
 * To load the image ito view using [Glide]
 */
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url)
        .placeholder(R.drawable.ic_cloud)
        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
        .into(imageView)
}