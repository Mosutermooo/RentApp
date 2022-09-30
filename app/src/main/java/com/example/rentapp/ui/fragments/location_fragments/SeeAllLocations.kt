package com.example.rentapp.ui.fragments.location_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentapp.R
import com.example.rentapp.adapters.LocationsAdapter
import com.example.rentapp.databinding.FragmentSeeAllLocationsBinding
import com.example.rentapp.ui.activies.LocationActivity
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.hideProgressDialog
import com.example.rentapp.uitls.Resources.showProgressDialog
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.LocationViewModel


class SeeAllLocations : Fragment() {

    private lateinit var binding: FragmentSeeAllLocationsBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationAdapter: LocationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSeeAllLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationAdapter = LocationsAdapter()
        locationViewModel = Initializers().initLocationViewModel(requireActivity() as AppCompatActivity)
        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.locations),
            getString(R.string.locations_fragment_subtitle)
        )

        binding.Locations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationAdapter
        }

        getLocations()

        locationAdapter.setOnClickListener {
            val intent = Intent(requireActivity(), LocationActivity::class.java)
            intent.putExtra("lat", it.lat)
            intent.putExtra("lng", it.lng)
            intent.putExtra("locationName", it.location_name)
            startActivity(intent)
        }






    }

    private fun getLocations() {
        locationViewModel.getAllLocations()
        lifecycleScope.launchWhenStarted {
            locationViewModel.getAllLocationsState.collect{
                when(it){
                    is Resource.Error -> {
                        hideProgressDialog()
                        it.message?.let { message ->
                            showSnackBar(message, requireActivity() as AppCompatActivity)
                        }
                    }
                    is Resource.Idle -> {}
                    is Resource.Internet -> {
                        showSnackBar(
                            it.message!!,
                            requireActivity() as AppCompatActivity
                        )
                    }
                    is Resource.Loading -> {
                        showProgressDialog()
                    }
                    is Resource.Success -> {
                        hideProgressDialog()
                        it.data?.let { locationResponse ->
                            locationAdapter.differ.submitList(locationResponse.Locations)
                        }
                    }
                }
            }
        }
    }


}