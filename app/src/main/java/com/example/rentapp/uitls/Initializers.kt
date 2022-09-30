package com.example.rentapp.uitls

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rentapp.view_models.CarViewModel
import com.example.rentapp.view_models.LocationViewModel
import com.example.rentapp.view_models.RentViewModel

class Initializers {
    fun initRentViewModel(activity: AppCompatActivity): RentViewModel {
       return ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application).create(
            RentViewModel::class.java)
    }

    fun initLocationViewModel(activity: AppCompatActivity): LocationViewModel {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application).create(
            LocationViewModel::class.java)
    }

    fun initCarViewModel(activity: AppCompatActivity): CarViewModel {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application).create(
            CarViewModel::class.java)
    }

}