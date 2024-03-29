package com.example.dummy

import android.app.Application
import com.example.dummy.api.RetrofitHelper
import com.example.dummy.api.RetrofitHelper2
import com.example.dummy.db.WeatherDatabase
import com.example.dummy.repository.WeatherRepository

class WeatherApplication: Application() {
    lateinit var weatherRepository: WeatherRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    fun initialize(){
        val api = RetrofitHelper.api
        val api2 = RetrofitHelper2.api
        val database = WeatherDatabase(applicationContext)
        weatherRepository = WeatherRepository(api,api2,database,applicationContext)
    }
}