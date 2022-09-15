package com.example.models

import java.io.Serializable


data class InsertCarRequestParams (
    val car_Brand: String,
    val car_Model: String,
    val car_Type: String,
    val totalPrice: Int,
    val status: String,
    val carImage: List<CarImageRequestParams>,
    val userId: String
        ): Serializable