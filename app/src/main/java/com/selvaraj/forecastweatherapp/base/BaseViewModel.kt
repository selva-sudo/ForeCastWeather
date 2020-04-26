package com.selvaraj.forecastweatherapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.selvaraj.forecastweatherapp.model.UiState

open class BaseViewModel : ViewModel() {

    fun uiState(): LiveData<UiState> = uiState
    protected val uiState: MutableLiveData<UiState> = MutableLiveData()
}