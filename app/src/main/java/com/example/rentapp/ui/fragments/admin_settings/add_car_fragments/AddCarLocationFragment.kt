package com.example.rentapp.ui.fragments.admin_settings.add_car_fragments

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
import androidx.viewpager2.widget.ViewPager2
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentAddCarLocationBinding
import com.example.rentapp.ui.dialogs.OnClick
import com.example.rentapp.ui.dialogs.ShowLocationsDialog
import com.example.rentapp.uitls.GlobalState
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.CarViewModel
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlin.math.ln


class AddCarLocationFragment : Fragment(), OnClick {

    private lateinit var binding: FragmentAddCarLocationBinding
    private lateinit var carViewModel: CarViewModel
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddCarLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val car = GlobalState.cachedCarUpload
        carViewModel = Initializers().initCarViewModel(requireActivity() as AppCompatActivity)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        if (car != null){
            if(car.carLocationLat.isEmpty() && car.carLocationLng.isEmpty()){
                binding.location.setText("")
            }else{
                lat = car.carLocationLat.toDouble()
                lng = car.carLocationLng.toDouble()
                getAddressFromLatLng()
            }

        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.back.setOnClickListener {
            viewPager?.currentItem = 1
        }

        binding.next.setOnClickListener {
            if(lat == null && lng == null){
                showSnackBar(
                    "Please select a location",
                    requireActivity() as AppCompatActivity
                )
                return@setOnClickListener
            }
            carViewModel.changeCachedUploadCarLocation(car?.id.toString(), lat.toString(), lng.toString())
            viewPager?.currentItem = 3
        }

        binding.selectALocation.setOnClickListener {
            ShowLocationsDialog(this)
                .show(requireFragmentManager(), "LOCATIONS_DIALOG")
        }

        binding.currentLocation.setOnClickListener {
            if(!isLocationOn()){
                Resources.showSnackBar(
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

        binding.currentLocation.isFocusable = false
        binding.location.isFocusable = false
        binding.selectALocation.isFocusable = false



    }

    private fun isLocationOn(): Boolean{
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gpsProvider || networkProvider
    }

    fun getAddressFromLatLng(){
        val address = Resources.getAddressFromLatLng(lat ?: return, lng ?: return, requireContext())
        address?.let {
            val sb = StringBuilder()
            for (i in 0 until address.maxAddressLineIndex) {
                sb.append(address.getAddressLine(i))
            }
            sb.append(address.countryName).append(", ")
            sb.append(address.postalCode ?: "No Postal Code").append(", ")
            sb.append(address.locality ?: "No Locality")
            binding.location.setText(sb.toString())
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
            lat = lastLocation.latitude
            lng = lastLocation.longitude
            getAddressFromLatLng()
        }
    }

    override fun onClick(lat: String, lng: String) {
        this.lat = lat.toDouble()
        this.lng = lng.toDouble()
        getAddressFromLatLng()
    }

    override fun onActivityResultReceiver(lat: String?, lng: String?, address: String?) {
        if(lat != null && lng != null && address != null){
            this.lat = lat.toDouble()
            this.lng = lng.toDouble()
            binding.location.setText(address)
        }
    }





}