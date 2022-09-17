package com.example.rentapp.repositories

import com.example.models.LoginParams
import com.example.models.LoginResponse
import com.example.models.RegisterResponse
import com.example.models.RegisterUserParams
import okhttp3.ResponseBody
import retrofit2.Response

class AuthRepositoryTestImpl : AuthRepository {
    override suspend fun signUpUser(params: RegisterUserParams): Response<RegisterResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(params: LoginParams): Response<LoginResponse> {
        if(params.username == "1" && params.password == "2"){
            return Response.success(LoginResponse(true, "Successfully logged in", "123123", "123123", "admin"))
        }
        return Response.success(LoginResponse(false, "Successfully logged in", "123123", "123123", "admin"))
    }
}