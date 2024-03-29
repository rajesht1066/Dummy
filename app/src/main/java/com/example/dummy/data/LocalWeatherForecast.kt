package com.example.dummy.data

data class LocalWeatherForecast (
    val weatherData: LocalWeather,
    val forecastData: List<LocalForecast>
)