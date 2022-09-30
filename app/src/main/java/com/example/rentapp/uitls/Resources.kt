package com.example.rentapp.uitls

import android.app.Dialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.rentapp.R
import com.google.android.material.snackbar.Snackbar
import java.util.*

object Resources {

    private lateinit var dialog: Dialog

    fun initProgressDialog(context: Context){
        dialog = Dialog(context)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
    fun showProgressDialog(){
        dialog.show()
    }
    fun hideProgressDialog(){
        dialog.dismiss()
    }

    fun showSnackBar(text: String, activity: AppCompatActivity){
        val snackBar = Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT)
        snackBar.show()
    }

    fun setupToolBar(toolbar: Toolbar, activity: AppCompatActivity, title: String? = null, subTitle: String? = null){
        activity.setSupportActionBar(toolbar)
        val actionBar = activity.supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar.title = title
            actionBar.subtitle = subTitle
        }
        toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }

    fun millisToDays(millis: Long): Long{
        return millis / 86400000L
    }

    fun getAddressFromLatLng(lat: Double, lng: Double, context: Context): Address? {
        val geocoder = Geocoder(context.applicationContext, Locale.getDefault())
        var result: String? = null
        val addressList: List<Address>? = geocoder.getFromLocation(lat, lng, 1)
        if (addressList != null && addressList.size > 0) {
            return addressList[0]
        }
        return null
    }

    fun getRandomNumberId() : Int = System.currentTimeMillis().toInt()

}