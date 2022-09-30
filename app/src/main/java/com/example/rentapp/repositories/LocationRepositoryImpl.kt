package com.example.rentapp.repositories

import com.example.rentapp.models.locations_model.LocationModelRequestParams
import com.example.rentapp.models.locations_model.LocationModelResponseParams
import com.example.rentapp.network.ApiService
import retrofit2.Response

class LocationRepositoryImpl(
    private val apiService: ApiService
) : LocationRepository{
    override suspend fun addLocation(params: LocationModelRequestParams): Response<LocationModelResponseParams> {
        return apiService.addLocation(params)
    }

    override suspend fun deleteLocation(id: Int): Response<LocationModelResponseParams> {
        return apiService.deleteLocation(id)
    }

    override suspend fun allLocations(): Response<LocationModelResponseParams> {
        return apiService.allLocations()
    }


}