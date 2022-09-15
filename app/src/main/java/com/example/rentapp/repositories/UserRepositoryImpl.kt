package com.example.rentapp.repositories

import com.example.models.UserResponseParams
import com.example.rentapp.db.DbService
import com.example.rentapp.network.ApiService
import retrofit2.Response

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val dbService: DbService
) : UserRepository {
    override suspend fun getUserData(userId: String): Response<UserResponseParams> {
       return apiService.getUserData(userId)
    }
}