package com.example.rentapp.ui.fragments.location_fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentAddLocationBinding
import com.example.rentapp.ui.activies.AddLocationActivity
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.hideProgressDialog
import com.example.rentapp.uitls.Resources.showProgressDialog
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.LocationViewModel
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.flow.collect

class AddLocationFragment() : Fragment() {

    private lateinit var binding: FragmentAddLocationBinding
    private lateinit var locationViewModel: LocationViewModel
    private var lat: String? = null
    private var lng: String? = null
    private var address: String? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        locationViewModel = Initializers().initLocationViewModel(requireActivity() as AppCompatActivity)

        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.add_a_new_location)
        )

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.location.isFocusable = false
        binding.currentLocation.isFocusable = false

        binding.location.setOnClickListener {
            val intent = Intent(requireActivity(), AddLocationActivity::class.java)
            startActivityForResult(intent, 200)
        }

        binding.currentLocation.setOnClickListener {
            if(!isLocationOn()){
                showSnackBar(
                    getString(R.string.turn_on_location),
                    requireActivity() as AppCompatActivity
                )
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }else{
                Dexter.withContext(requireContext())
                    .withPermissions(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ).withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if(report!!.areAllPermissionsGranted()){
                                requestNewLocationData()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: MutableList<PermissionRequest>?,
                            p1: PermissionToken?,
                        ) {
                            p1?.continuePermissionRequest()
                        }
                    }).onSameThread().check()
            }
        }
        binding.AddLocation.setOnClickListener {
            locationViewModel.addLocation(
                lat,
                lng,
                binding.locationName.text.toString(),
                binding.locationCity.text.toString(),
            )
            lifecycleScope.launchWhenStarted {
                locationViewModel.addLocationState.collect{
                    when(it){
                        is Resource.Error -> {
                            hideProgressDialog()
                            it.message?.let { message ->
                                showSnackBar(message, requireActivity() as AppCompatActivity)
                            }
                        }
                        is Resource.Idle -> {}
                        is Resource.Internet -> {
                            hideProgressDialog()
                            it.message?.let { message ->
                                showSnackBar(message, requireActivity() as AppCompatActivity)
                            }
                        }
                        is Resource.Loading -> {
                            showProgressDialog()
                        }
                        is Resource.Success -> {
                            hideProgressDialog()
                            requireActivity().onBackPressed()
                            showSnackBar(
                                "Location Successfully added", requireActivity() as AppCompatActivity
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(){
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1000
        mLocationRequest.numUpdates = 1
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, Looper.myLooper())
    }

    private val mLocationCallBack = object : LocationCallback(){
        override fun onLocationResult(location: LocationResult) {
            val lastLocation: Location = location.lastLocation!!
            lat = lastLocation.latitude.toString()
            lng = lastLocation.longitude.toString()
            val address = Resources.getAddressFromLatLng(lat!!.toDouble(), lng!!.toDouble(), requireActivity() as AppCompatActivity)
            address?.let {
                val sb = StringBuilder()
                for (i in 0 until address.getMaxAddressLineIndex()) {
                    sb.append(address.getAddressLine(i)) //.append("\n");
                }
                sb.append(address.countryName).append(", ")
                sb.append(address.postalCode ?: "No Postal Code").append(", ")
                sb.append(address.locality ?: "No Locality")
                this@AddLocationFragment.address = sb.toString()
            }
            binding.location.setText(this@AddLocationFragment.address)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            when(requestCode){
                200 -> {
                    val lat = data.getStringExtra("latitude")
                    val lng = data.getStringExtra("longitude")
                    this.lat = lat
                    this.lng = lng
                    val address = data.getStringExtra("address")
                    binding.location.setText("$address")
                }
            }
        }
    }

    private fun isLocationOn(): Boolean{
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gpsProvider || networkProvider
    }



}