package com.example.dummy.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dummy.R
import com.example.dummy.data.LocalLocation
import com.example.dummy.databinding.FragmentSearchBinding
import com.example.dummy.viewmodel.WeatherViewModel

class RecyclerAdapter(
    private val binding: FragmentSearchBinding,
    private val viewModel: WeatherViewModel,
    private val data: ArrayList<LocalLocation>
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = data[position].label
        holder.cityName.setOnClickListener {
            Log.d("check","Btn clicked")
//            binding.editText.text = data[position].label.toEditable()
            viewModel.getCurrentWeather(data[position].label,data[position].city)
            Navigation.findNavController(holder.itemView).navigateUp()
        }
    }
//    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cityName = itemView.findViewById<TextView>(R.id.cityName)
    }
}