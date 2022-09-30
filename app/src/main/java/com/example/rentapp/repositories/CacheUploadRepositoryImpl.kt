package com.example.rentapp.repositories

import androidx.lifecycle.LiveData
import com.example.rentapp.db.DbService
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.models.add_car_cache_models.CachedCarImages

class CacheUploadRepositoryImpl(
    val dbService: DbService
) : CacheUploadRepository {
    override suspend fun addACarUploadCache(car: CacheUploadCarModel) {
        dbService.addACarUploadCache(car)
    }

    override fun getAllCacheCarUploads(): LiveData<List<CacheUploadCarModel>> {
        return dbService.getAllCacheCarUploads()
    }

    override fun getSingleCacheCarUpload(id: Int): LiveData<CacheUploadCarModel> {
        return dbService.getSingleCacheCarUpload(id)
    }

    override suspend fun changeCachedUploadCarDetails(
        cachedCarId: String,
        carBrand: String,
        carModel: String,
        carType: String,
    ) {
        dbService.changeCachedUploadCarDetails(
            cachedCarId,
            carBrand,
            carModel,
            carType
        )
    }

    override suspend fun changeCachedUploadCarPrice(cachedCarId: String, carPrice: String) {
        dbService.changeCachedUploadCarPrice(
            cachedCarId,
            carPrice
        )
    }

    override suspend fun changeCachedUploadCarLocation(
        cachedCarId: String,
        lat: String,
        lng: String,
    ) {
        dbService.changeCachedUploadCarLocation(
            cachedCarId,
            lat,
            lng
        )
    }

    override suspend fun addCarImages(carImage: CachedCarImages) {
        dbService.addCarImages(carImage)
    }

    override fun getAllCachedCarImagesByCarId(carId: Int): LiveData<List<CachedCarImages>> {
        return dbService.getAllCachedCarImagesByCarId(carId)
    }
}