package com.example.dummy.api

import com.example.dummy.data.Weather.Places
import com.example.dummy.data.Weather.Weather
import com.example.dummy.data.forecast.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CustomApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ) : Response<Weather>

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Response<Forecast>

    @GET("autocomplete")
    suspend fun getCity(
        @Query("q") pattern: String,
        @Query("apiKey") apiKey: String
    ) : Response<Places>
}