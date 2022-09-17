package com.example.models

import java.io.Serializable


data class RegisterUserParams (
    val email: String,
    val username: String,
    val password: String,
        ): Serializable