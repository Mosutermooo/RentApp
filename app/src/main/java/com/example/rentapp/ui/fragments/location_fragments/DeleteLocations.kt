package com.example.rentapp.ui.fragments.location_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentapp.R
import com.example.rentapp.adapters.LocationsAdapter
import com.example.rentapp.databinding.FragmentDeleteLocationsBinding
import com.example.rentapp.models.locations_model.LocationModel
import com.example.rentapp.ui.activies.LocationActivity
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.view_models.LocationViewModel
import com.google.android.material.snackbar.Snackbar


class DeleteLocations : Fragment() {

    private lateinit var binding: FragmentDeleteLocationsBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationsAdapter: LocationsAdapter
    private lateinit var locations: MutableList<LocationModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDeleteLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationViewModel = Initializers().initLocationViewModel(requireActivity() as AppCompatActivity)
        locationsAdapter = LocationsAdapter()
        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.delete_locations_title),
            getString(R.string.delete_location_subtitle)
        )
        val itemTouchHelper = ItemTouchHelper(simpleTouchCallBack)
        itemTouchHelper.attachToRecyclerView(binding.Locations)
        binding.Locations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationsAdapter

        }

        getLocations()


        locationsAdapter.setOnClickListener {
            val intent = Intent(requireActivity(), LocationActivity::class.java)
            intent.putExtra("lat", it.lat)
            intent.putExtra("lng", it.lng)
            intent.putExtra("locationName", it.location_name)
            startActivity(intent)
        }




    }


    val simpleTouchCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val location = locations.elementAt(viewHolder.adapterPosition)
            locations.remove(location)
            locationsAdapter.notifyDataSetChanged()
            locationViewModel.deleteLocation(location.id)
        }

    }


    private fun getLocations() {
        locationViewModel.getAllLocations()
        lifecycleScope.launchWhenStarted {
            locationViewModel.getAllLocationsState.collect{
                when(it){
                    is Resource.Error -> {
                        Resources.hideProgressDialog()
                        it.message?.let { message ->
                            Resources.showSnackBar(message, requireActivity() as AppCompatActivity)
                        }
                    }
                    is Resource.Idle -> {}
                    is Resource.Internet -> {
                        Resources.showSnackBar(
                            it.message!!,
                            requireActivity() as AppCompatActivity
                        )
                    }
                    is Resource.Loading -> {
                        Resources.showProgressDialog()
                    }
                    is Resource.Success -> {
                        Resources.hideProgressDialog()
                        it.data?.let { locationResponse ->
                            locations = locationResponse.Locations
                            locationsAdapter.differ.submitList(locations)
                        }
                    }
                }
            }
        }
    }

}