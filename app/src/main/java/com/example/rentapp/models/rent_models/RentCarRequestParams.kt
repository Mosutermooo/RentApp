package com.example.models

import java.io.Serializable


data class RentCarRequestParams(
    val carId: Int,
    val userId: String,
    val price: Int,
    val rentTime: Long
) : Serializable
