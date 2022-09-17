package com.example.models

import java.io.Serializable


data class RentCarResponseParams(
    val success: Boolean,
    val message: String,
    val rentId: Long?,
    val rents: List<AllRentsResponseParams>? = null
) : Serializable
