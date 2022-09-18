package com.example.rentapp.models.car_models

import java.io.Serializable

data class FastSelectPlans(
    val price: Int,
    val days: Long,
    val name: String
): Serializable
