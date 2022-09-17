package com.example.rentapp.repositories

import com.example.models.LoginParams
import com.example.models.LoginResponse
import com.example.models.RegisterResponse
import com.example.models.RegisterUserParams
import retrofit2.Response

interface AuthRepository {
    suspend fun signUpUser(params: RegisterUserParams) : Response<RegisterResponse>
    suspend fun loginUser(params: LoginParams) : Response<LoginResponse>
}