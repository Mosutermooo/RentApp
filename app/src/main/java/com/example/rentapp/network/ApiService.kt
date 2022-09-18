package com.example.rentapp.network

import com.example.models.*
import retrofit2.Response
import retrofit2.http.*

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

    @POST("auth/register")
    suspend fun signUpUser(
        @Body params: RegisterUserParams
    ) : Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(
        @Body params: LoginParams
    ) : Response<LoginResponse>


    @POST("rent/car")
    suspend fun rentACar(
        @Body params: RentCarRequestParams
    ): Response<RentCarResponseParams>

    @GET("rent/carStatus")
    suspend fun getCarStatus(
        @Query("carId") carId: Int
    ) : Response<String>


}