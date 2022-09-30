package com.example.rentapp.network

import com.example.models.*
import com.example.rentapp.models.locations_model.LocationModelRequestParams
import com.example.rentapp.models.locations_model.LocationModelResponseParams
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

    @GET("cars/userRents/{userId}")
    suspend fun getUserRents(
        @Path("userId") userId: String
    ) : Response<RentsResponseParams>

    @GET("cars/rents/rentByRentId/{rentId}")
    suspend fun getRentsByRentId(
        @Path("rentId") rentId: String
    ) : Response<RentsResponseParams>

    @GET("cars/rents")
    suspend fun getAllRents(): Response<RentsResponseParams>


    @POST("admin/addLocation")
    suspend fun addLocation(
        @Body params: LocationModelRequestParams
    ): Response<LocationModelResponseParams>

    @DELETE("user/location/{id}")
    suspend fun deleteLocation(
        @Path("id") id: Int
    ): Response<LocationModelResponseParams>

    @GET("user/locations")
    suspend fun allLocations(): Response<LocationModelResponseParams>


}