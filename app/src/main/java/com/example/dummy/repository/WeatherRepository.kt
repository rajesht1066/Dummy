package com.example.dummy.repository

import android.content.Context
import android.util.Log
import com.example.dummy.api.CustomApi
import com.example.dummy.data.LocalForecast
import com.example.dummy.data.LocalLocation
import com.example.dummy.data.LocalWeather
import com.example.dummy.data.forecast.Forecast
import com.example.dummy.db.WeatherDatabase
import com.example.dummy.utils.DataTypeConverter
import com.example.dummy.utils.Network
import java.lang.Exception

class WeatherRepository(
    private val api: CustomApi,
    private val api2: CustomApi,
    private val weatherDatabase: WeatherDatabase,
    private val applicationContext: Context
) {
    private val key = "88c0154a25fb21fb0d30003e6956fb4c"
    private val key2 = "fKzVotCk8CmC2teZ3QEn0f9XoL_sZAV585wgTMKFoao"

    suspend fun getCurrentWeather(label: String, city: String?): Response<LocalWeather>{
        if (Network.isNetworkAvailable(applicationContext)){
            try {
                Log.d("check",label+city)
                val apiData = api.getCurrentWeather(label,key)
                var weather: LocalWeather? = null
                apiData?.body()?.let {
                    weather = DataTypeConverter.convert(label,city,it)
                    weatherDatabase.getWeatherDao().upsertCityWeather(weather!!)
                }
                if(apiData.code() == 404){
                    val apiData2 = city?.let { api.getCurrentWeather(it,key) }
                    apiData2?.body()?.let {
                        weather = DataTypeConverter.convert(label,city,it)
                        weatherDatabase.getWeatherDao().upsertCityWeather(weather!!)
                    }
                }
                Log.d("check",apiData.toString())
                return Response.Success(weather)
            }
            catch (e: Exception){
                Log.d("error",e.printStackTrace().toString())
                return Response.Failure(e.message.toString())
            }
        } else{
            return Response.Success(weatherDatabase.getWeatherDao().getCityWeather(label),"You are Offline!")
        }
    }

    suspend fun getForecast(label:String, city:String?): Response<List<LocalForecast>> {
        if (Network.isNetworkAvailable(applicationContext)){
            try {
                val apiData = api.getForecast(label,key)
                var forecast: List<LocalForecast>? = null
                apiData?.body()?.let {
                    forecast = DataTypeConverter.convert2(it)
                }
                if(apiData.code() == 404){
                    val apiData2 = city?.let { api.getForecast(it,key) }
                    apiData2?.body()?.let {
                        forecast = DataTypeConverter.convert2(it)
                    }
                }
                Log.d("check",apiData.toString())
                return Response.Success(forecast)
            }
            catch (e: Exception){
                Log.d("error",e.printStackTrace().toString())
                return Response.Failure(e.message.toString())
            }
        }
        else{
            return Response.Success(null,"You are Offline!")
        }
    }

    suspend fun getCity(pattern:String) : Response<List<LocalLocation>>{
        if (Network.isNetworkAvailable(applicationContext)){
            try {
                Log.d("check",pattern)
                val locationList = ArrayList<LocalLocation>()
                val apiData = api2.getCity(pattern,key2)
                apiData?.body()?.let {
                    val items = apiData.body()?.items
                    items?.let {
                        for (i in items){
                            locationList.add(LocalLocation(i.address.label,i.address.city))
                        }
                    }
                }
                Log.d("check",apiData.toString())
                return Response.Success(locationList)
            }
            catch (e: Exception){
                Log.d("error",e.printStackTrace().toString())
                return Response.Failure(e.message.toString())
            }
        } else{
            return Response.Success(weatherDatabase.getWeatherDao().getAllCities(),"You are Offline!")
        }
    }
}