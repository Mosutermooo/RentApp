package com.example.rentapp.uitls

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.rentapp.R
import com.google.android.material.snackbar.Snackbar

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

}