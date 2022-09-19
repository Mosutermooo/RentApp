package com.example.models

import java.io.Serializable

data class RentsResponseParams(
    var success: Boolean,
    val message: String,
    val rents: List<AllRentsResponseParams>? = null
): Serializable