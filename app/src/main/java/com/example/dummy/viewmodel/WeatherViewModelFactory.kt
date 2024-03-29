package com.example.dummy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dummy.repository.WeatherRepository

class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(WeatherRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: Exception) {
            Log.e("Error",e.message.toString())
        }
        return super.create(modelClass)
    }
}