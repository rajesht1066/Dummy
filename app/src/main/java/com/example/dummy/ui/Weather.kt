package com.example.dummy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dummy.R
import com.example.dummy.adapters.RvAdapter
import com.example.dummy.databinding.BottomSheetLayoutBinding
import com.example.dummy.databinding.FragmentWeatherBinding
import com.example.dummy.viewmodel.WeatherViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class Weather : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var sheetlayoutBinding: BottomSheetLayoutBinding
    private lateinit var dialog: BottomSheetDialog
    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false);
        sheetlayoutBinding = DataBindingUtil.inflate(inflater,R.layout.bottom_sheet_layout,container,false)
        dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetTheme)
        dialog.setContentView(sheetlayoutBinding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.weatherforecast.collect{
                binding.data = it.data?.weatherData
                Log.d("UI check",it.data.toString())
                var avgTemp = 0.0
                var avgPressure = 0.0
                var avgHumidity = 0.0
                var count = 0
                for (i in it.data?.forecastData!!){
                    count++
                    avgTemp+=i.temp
                    avgPressure+=i.pressure
                    avgHumidity+=i.humidity
                }
                binding.avgTemp.text = (avgTemp/count).roundToInt().toString()
                binding.avgPressure.text = (avgPressure/count).toString()
                binding.avgHumidity.text = (avgHumidity/count).toString()
            }
        }

        binding.searchView.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_weather_to_search)
        }

        binding.forecast.setOnClickListener {
            openDialog(binding.data!!.label,binding.data?.city)
        }
    }

    private fun openDialog(label: String, city: String?) {
        viewModel.getForecast(label,city)
        lifecycleScope.launch {
            viewModel.forecast.collect{
                Log.d("check",it?.data.toString())
                it.data?.let {
                    val adapter = RvAdapter(it)
                    sheetlayoutBinding.rvForecast.apply {
                        this.adapter = adapter
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(context,1,RecyclerView.HORIZONTAL,false)
                    }
                }
                dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
                dialog.show()
            }
        }
    }
}