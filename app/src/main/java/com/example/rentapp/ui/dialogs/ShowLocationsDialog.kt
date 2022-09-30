package com.example.rentapp.ui.dialogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentapp.adapters.LocationsAdapter
import com.example.rentapp.databinding.DialogLocationViewBinding
import com.example.rentapp.ui.activies.AddLocationActivity
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.view_models.LocationViewModel

class ShowLocationsDialog(
    private val onClickInterface: OnClick,
) : DialogFragment() {

    private lateinit var locationsAdapter: LocationsAdapter
    private lateinit var binding: DialogLocationViewBinding
    private lateinit var locationViewModel: LocationViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogLocationViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationsAdapter = LocationsAdapter()
        locationViewModel = Initializers().initLocationViewModel(requireActivity() as AppCompatActivity)

        binding.Locations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationsAdapter
        }

        getLocations()

        locationsAdapter.setOnClickListener {
            onClickInterface.onClick(it.lat, it.lng)
            dismiss()
        }
        binding.close.setOnClickListener{
            dismiss()
        }

        binding.choseANewLocation.setOnClickListener {
            val intent = Intent(requireActivity(), AddLocationActivity::class.java)
            startActivityForResult(intent, 300)
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
                            locationsAdapter.differ.submitList(locationResponse.Locations)
                        }
                    }
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            when(requestCode){
                300 -> {
                    val lat = data.getStringExtra("latitude")
                    val lng = data.getStringExtra("longitude")
                    val address = data.getStringExtra("address")
                    onClickInterface.onActivityResultReceiver(lat, lng, address)
                    dismiss()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.setLayout(width, height)
    }


}

interface OnClick{
    fun onClick(lat: String, lng: String)
    fun onActivityResultReceiver(lat: String?, lng: String?, address: String?)
}