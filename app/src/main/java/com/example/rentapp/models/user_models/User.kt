package com.example.models

import java.io.Serializable


data class User (
    val userId: String,
    val name: String,
    val lastname: String,
    val email: String,
    val username: String,
    val createdAt: Long,
    val password: String? = null,
    val role: String
        ): Serializable