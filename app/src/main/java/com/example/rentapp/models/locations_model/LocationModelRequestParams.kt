package com.example.rentapp.models.locations_model

data class LocationModelRequestParams (
    val lat: String,
    val lng: String,
    val location_name: String,
    val city: String,
    val userId: String
        )