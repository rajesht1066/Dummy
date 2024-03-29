package com.example.dummy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dummy.data.LocalForecast
import com.example.dummy.data.LocalLocation
import com.example.dummy.data.LocalWeather
import com.example.dummy.data.LocalWeatherForecast
import com.example.dummy.data.forecast.Forecast
import com.example.dummy.repository.Response
import com.example.dummy.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository): ViewModel(){
    private val _weather = MutableSharedFlow<Response<LocalWeather>>(1)
    val weather: SharedFlow<Response<LocalWeather>>
        get() = _weather
    private val _weatherforecast = MutableSharedFlow<Response<LocalWeatherForecast>>(1)
    val weatherforecast: SharedFlow<Response<LocalWeatherForecast>>
        get() = _weatherforecast
    private val _forecast = MutableSharedFlow<Response<List<LocalForecast>>>(1)
    val forecast: SharedFlow<Response<List<LocalForecast>>>
        get() = _forecast
    private val _place = MutableSharedFlow<Response<List<LocalLocation>>>(1)
    val place: SharedFlow<Response<List<LocalLocation>>>
        get() = _place

    fun getCurrentWeather(label: String, city: String?){
        viewModelScope.launch {
            val weather = async { repository.getCurrentWeather(label,city) }
            val forecast = async { repository.getForecast(label,city) }
            val weatherForecast = LocalWeatherForecast(weather.await().data!!, forecast.await().data!!)
            _weatherforecast.emit(Response.Success(weatherForecast))
        }
    }

    fun getForecast(label: String, city: String?){
        viewModelScope.launch {
            val forecast = repository.getForecast(label,city)
            _forecast.emit(forecast)
        }
    }

    fun getCity(pattern: String){
        viewModelScope.launch {
            val cities = repository.getCity(pattern)
            _place.emit(cities)
        }
    }
}