package com.example.rentapp.repositories

import androidx.lifecycle.LiveData
import com.example.models.Car
import com.example.models.CarResponse
import retrofit2.Response

interface CarRepository {
    suspend fun getEveryCar() : Response<CarResponse>
    suspend fun getCarByType(type: String): Response<List<Car>>
    suspend fun cacheCars(cars : List<Car>)
    fun getCachedCars() : LiveData<List<Car>>
    suspend fun deleteCachedCars()
    suspend fun getCarByTypeDb(type: String): List<Car>

}