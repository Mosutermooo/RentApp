package com.example.rentapp.repositories

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.models.add_car_cache_models.CachedCarImages

interface CacheUploadRepository {
    suspend fun addACarUploadCache(car: CacheUploadCarModel)
    fun getAllCacheCarUploads() : LiveData<List<CacheUploadCarModel>>
    fun getSingleCacheCarUpload(id: Int): LiveData<CacheUploadCarModel>
    suspend fun changeCachedUploadCarDetails(
        cachedCarId: String,
        carBrand: String,
        carModel: String,
        carType: String
    )
    suspend fun changeCachedUploadCarPrice(cachedCarId: String,carPrice: String)
    suspend fun changeCachedUploadCarLocation(
        cachedCarId: String,
        lat: String,
        lng: String
    )
    suspend fun addCarImages(carImage: CachedCarImages)
    fun getAllCachedCarImagesByCarId(carId: Int) : LiveData<List<CachedCarImages>>
}