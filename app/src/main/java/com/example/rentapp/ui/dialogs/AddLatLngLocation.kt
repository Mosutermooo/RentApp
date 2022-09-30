package com.example.rentapp.ui.dialogs

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.rentapp.databinding.DialogAddLatLngLocationLayoutBinding
import com.example.rentapp.uitls.Resources
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.launch
import java.util.*


class AddLatLngLocation(private var marker: Marker) : DialogFragment() {

    private lateinit var binding: DialogAddLatLngLocationLayoutBinding
    private lateinit var addressStr: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogAddLatLngLocationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val address = Resources.getAddressFromLatLng(
            marker.position.latitude,
            marker.position.longitude,
            requireActivity() as AppCompatActivity
        )

        address?.let {
            val sb = StringBuilder()
            for (i in 0 until address.getMaxAddressLineIndex()) {
                sb.append(address.getAddressLine(i)) //.append("\n");
            }
            sb.append(address.countryName).append(", ")
            sb.append(address.postalCode ?: "No Postal Code").append(", ")
            sb.append(address.locality ?: "No Locality")
            addressStr = sb.toString()
        }

        binding.adress.text = addressStr
        binding.add.setOnClickListener {
            val intent = Intent()
            intent.putExtra("latitude", marker.position.latitude.toString())
            intent.putExtra("longitude", marker.position.longitude.toString())
            intent.putExtra("address", addressStr)
            requireActivity().setResult(Activity.RESULT_OK, intent)
            requireActivity().onBackPressed()

        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

    }





}