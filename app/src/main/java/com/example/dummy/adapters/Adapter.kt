package com.example.dummy.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.dummy.utils.dateFormatConverter

@BindingAdapter("imageFromUrl")
fun ImageView.imageFromUrl(url: String){
    Glide.with(this.context).load(url).into(this)
}

@BindingAdapter("setDateFormat")
fun TextView.setDateFormat(value: Int){
    this.text = dateFormatConverter(value.toLong())
}