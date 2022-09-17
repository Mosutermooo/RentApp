package com.example.rentapp.repositories

import com.example.models.LoginParams
import com.example.models.LoginResponse
import com.example.models.RegisterResponse
import com.example.models.RegisterUserParams
import com.example.rentapp.network.ApiService
import retrofit2.Response

class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun signUpUser(params: RegisterUserParams): Response<RegisterResponse> {
        return apiService.signUpUser(params)
    }

    override suspend fun loginUser(params: LoginParams): Response<LoginResponse> {
        return apiService.loginUser(params)
    }
}