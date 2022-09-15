package com.example.rentapp.network

import com.example.models.Car
import com.example.models.CarResponse
import com.example.models.UserResponseParams
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("cars/allCars")
    suspend fun getEveryCar() : Response<CarResponse>

    @GET("cars/carType/{carType}")
    suspend fun getCarByType(
        @Path("carType") type: String
    ) : Response<List<Car>>

    @GET("user/userData/{userId}")
    suspend fun getUserData(
        @Path("userId") userId: String
    ) : Response<UserResponseParams>

}