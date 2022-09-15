package com.example.models


import java.io.Serializable


data class CarImage(
    val id: Int,
    val imageUrl: String,
    val carId: Int
): Serializable
