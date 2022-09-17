package com.example.models

import java.io.Serializable


data class LoginParams(
    val username: String,
    val password: String
): Serializable
