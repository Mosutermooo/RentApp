package com.example.models

import java.io.Serializable


data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val userId: String?,
    val role: String
        ): Serializable