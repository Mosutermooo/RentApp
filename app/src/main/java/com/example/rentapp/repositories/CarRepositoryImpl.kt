package com.example.rentapp.repositories

import androidx.lifecycle.LiveData
import com.example.models.Car
import com.example.models.CarResponse
import com.example.rentapp.db.DbService
import com.example.rentapp.network.ApiService
import retrofit2.Response

class CarRepositoryImpl (
    private val apiService: ApiService,
    private val dbService: DbService
    ): CarRepository {
    override suspend fun getEveryCar(): Response<CarResponse> {
       return apiService.getEveryCar()
    }

    override suspend fun getCarByType(type: String): Response<List<Car>> {
        return apiService.getCarByType(type)
    }

    override suspend fun getCarByTypeDb(type: String): List<Car> {
        return dbService.getCarByType(type)
    }

    override suspend fun cacheCars(cars: List<Car>) {
        dbService.cacheCars(cars)
    }

    override fun getCachedCars(): LiveData<List<Car>> {
        return dbService.getCachedCars()
    }

    override suspend fun deleteCachedCars() {
        dbService.deleteCachedCars()
    }


}