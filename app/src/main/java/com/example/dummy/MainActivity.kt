package com.example.dummy

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.dummy.databinding.ActivityMainBinding
import com.example.dummy.repository.WeatherRepository
import com.example.dummy.utils.Location
import com.example.dummy.utils.LocationPermission
import com.example.dummy.utils.UserPermission
import com.example.dummy.viewmodel.WeatherViewModel
import com.example.dummy.viewmodel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: WeatherRepository
    private lateinit var factory: WeatherViewModelFactory
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        repository = (application as WeatherApplication).weatherRepository
        factory = WeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(WeatherViewModel::class.java)

        val userPermission = LocationPermission(this)
        userPermission.checkPermission()
        userPermission.requestPermission()
        Location(this).getCurrentLocation(viewModel)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == UserPermission.PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Granted", Toast.LENGTH_SHORT).show()
                Location(this).getCurrentLocation(viewModel)
            }
            else{
                Toast.makeText(this,"Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}