package com.example.rentapp.repositories

import com.example.models.UserResponseParams
import retrofit2.Response

interface UserRepository {
    suspend fun getUserData(userId: String) : Response<UserResponseParams>
}