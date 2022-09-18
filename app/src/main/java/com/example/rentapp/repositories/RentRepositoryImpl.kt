package com.example.rentapp.repositories

import com.example.models.RentCarRequestParams
import com.example.models.RentCarResponseParams
import com.example.rentapp.network.ApiService
import retrofit2.Response

class RentRepositoryImpl(
    private var apiService: ApiService
) : RentRepository {
    override suspend fun rentACar(params: RentCarRequestParams): Response<RentCarResponseParams> {
        return apiService.rentACar(params)
    }


    override suspend fun getCarStatus(carId: Int): Response<String> {
        return apiService.getCarStatus(carId)
    }
}