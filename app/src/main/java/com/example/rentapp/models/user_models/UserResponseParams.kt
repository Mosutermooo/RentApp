package com.example.models

import java.io.Serializable


data class UserResponseParams (
    val success: Boolean,
    val message: String,
    val user: User?
        ): Serializable