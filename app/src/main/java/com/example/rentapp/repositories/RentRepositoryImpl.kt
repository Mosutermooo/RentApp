package com.example.rentapp.repositories

import com.example.models.RentCarRequestParams
import com.example.models.RentCarResponseParams
import com.example.models.RentsResponseParams
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

    override suspend fun getUserRents(userId: String): Response<RentsResponseParams> {
        return apiService.getUserRents(userId)
    }

    override suspend fun getRentsByRentId(rentId: String): Response<RentsResponseParams> {
        return apiService.getRentsByRentId(rentId)
    }

    override suspend fun getAllRents(): Response<RentsResponseParams> {
        return apiService.getAllRents()
    }
}