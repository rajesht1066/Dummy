package com.example.dummy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dummy.data.LocalForecast
import com.example.dummy.databinding.RvItemLayoutBinding
import com.example.dummy.repository.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RvAdapter( private val forecastArray: List<LocalForecast>) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    class ViewHolder(val binding : RvItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = forecastArray.get(position)
        holder.binding.apply {
            imgItem.imageFromUrl("https://openweathermap.org/img/w/${currentItem.icon}.png")
            tvItemTemp.text = "${currentItem.temp.toInt()} °C"
            tvItemStatus.text = "${currentItem.description}"
            tvItemTime.text = displayTime(currentItem.dt_txt)

        }
    }

    private fun displayTime(dtTxt: String): CharSequence? {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(dtTxt,input)
        return output.format(dateTime)

    }

    override fun getItemCount(): Int {
        return forecastArray.size
    }
}