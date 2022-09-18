package com.example.rentapp.repositories

import com.example.models.RegisterResponse
import com.example.models.RentCarRequestParams
import com.example.models.RentCarResponseParams
import retrofit2.Response
import retrofit2.http.Query

interface RentRepository {
    suspend fun rentACar(params: RentCarRequestParams): Response<RentCarResponseParams>
    suspend fun getCarStatus(carId: Int) : Response<String>
}