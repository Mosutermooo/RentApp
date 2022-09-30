package com.example.rentapp.models.locations_model

import com.example.rentapp.uitls.Resource

data class LocationModelResponseParams(
    val success: Boolean,
    val message: String,
    val Locations: MutableList<LocationModel>,
    val location : LocationModel
)
