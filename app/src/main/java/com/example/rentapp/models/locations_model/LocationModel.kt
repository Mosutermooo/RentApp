package com.example.rentapp.models.locations_model

data class LocationModel(
    val id: Int,
    val lat: String,
    val lng: String,
    val location_name: String,
    val city: String
)
