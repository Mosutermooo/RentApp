package com.example.rentapp.repositories

import com.example.rentapp.models.locations_model.LocationModelRequestParams
import com.example.rentapp.models.locations_model.LocationModelResponseParams
import retrofit2.Response

interface LocationRepository {
    suspend fun addLocation(params: LocationModelRequestParams): Response<LocationModelResponseParams>
    suspend fun deleteLocation(id: Int): Response<LocationModelResponseParams>
    suspend fun allLocations(): Response<LocationModelResponseParams>
}