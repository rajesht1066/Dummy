package com.example.dummy.data

data class LocalForecast (
    val icon: String,
    val description: String,
    val temp: Double,
    val humidity: Int,
    val pressure: Int,
    val dt_txt: String,
)