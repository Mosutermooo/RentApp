package com.example.models

import java.io.Serializable


data class CarResponse(
    val success: Boolean,
    val message: String,
    val data: List<Car>?
): Serializable
