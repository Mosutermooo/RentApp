package com.example.models

import java.io.Serializable


data class RegisterResponse(
    val success: Boolean,
    val message: String
): Serializable
