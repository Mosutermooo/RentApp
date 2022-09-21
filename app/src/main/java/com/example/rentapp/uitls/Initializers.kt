package com.example.rentapp.uitls

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rentapp.view_models.RentViewModel

class Initializers {
    fun initRentViewModel(activity: AppCompatActivity): RentViewModel {
       return ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application).create(
            RentViewModel::class.java)
    }

}