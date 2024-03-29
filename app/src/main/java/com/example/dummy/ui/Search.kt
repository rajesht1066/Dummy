package com.example.dummy.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dummy.R
import com.example.dummy.adapters.RecyclerAdapter
import com.example.dummy.data.LocalLocation
import com.example.dummy.databinding.FragmentSearchBinding
import com.example.dummy.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

class Search : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.place.collect{
                prepareRecyclerView(binding,viewModel, it.data!! as ArrayList<LocalLocation>)
            }
        }

        binding.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    if(p0.toString().length>=3){
                        viewModel.getCity(p0.toString())
                    }
                }
            }

        })

        binding.backButton.setOnClickListener{
            Navigation.findNavController(view).navigateUp()
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("check","Back Button Clicked")
                Navigation.findNavController(view).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun prepareRecyclerView(binding:FragmentSearchBinding,viewModel: WeatherViewModel, data: ArrayList<LocalLocation>){
        Log.d("check",data.toString())
        this.binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RecyclerAdapter(binding,viewModel,data)
        this.binding.recyclerView.adapter = adapter
    }
}