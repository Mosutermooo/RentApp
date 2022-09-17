package com.example.models

import java.io.Serializable

data class AllRentsResponseParams(
    val success: Boolean,
    val message: String,
    val price: Int?,
    val rentTime: Long?,
    val rentId: Long?,
    val car: Car?,
    val user: User?
        ): Serializable