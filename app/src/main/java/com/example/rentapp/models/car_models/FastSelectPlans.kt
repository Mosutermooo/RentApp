package com.example.rentapp.models.car_models

import java.io.Serializable

data class FastSelectPlans(
    val price: Int,
    val days: Int,
    val name: String
): Serializable
