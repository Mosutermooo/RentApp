package com.example.rentapp.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.models.Car
import com.example.models.CarResponse
import com.example.models.RentCarRequestParams
import com.example.rentapp.db.Database
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.models.add_car_cache_models.CachedCarImages
import com.example.rentapp.network.ApiInstance
import com.example.rentapp.repositories.CacheUploadRepositoryImpl
import com.example.rentapp.repositories.CarRepositoryImpl
import com.example.rentapp.uitls.NetworkConnection
import com.example.rentapp.uitls.NetworkConnectivity
import com.example.rentapp.uitls.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.launch

class CarViewModel(
    private val app: Application
) : AndroidViewModel(app) {

    private val apiService = ApiInstance.apiService(app)
    private val dbService = Database.database(app).dbService()
    private val repository = CarRepositoryImpl(apiService, dbService)
    private val addCarRepository: CacheUploadRepositoryImpl = CacheUploadRepositoryImpl(dbService)
    val everyCarState : MutableStateFlow<Resource<List<Car>>> = MutableStateFlow(Resource.Idle())
    val carByTypeState : MutableStateFlow<Resource<List<Car>>> = MutableStateFlow(Resource.Idle())

    fun getEveryCar() = viewModelScope.launch {
        everyCarState.emit(Resource.Loading())
        val connection = NetworkConnection().init(app)
        if(connection){
            getEveryCarSafeCall()
        }else{
            repository.getCachedCars().observeForever{ cars ->
                viewModelScope.launch {

                    if(cars.isNotEmpty() && cars != null){
                        everyCarState.emit(Resource.Success(cars))
                    }else{
                        everyCarState.emit(Resource.Error(null, "Error 44"))
                    }
                }
            }
        }

    }

    fun getEveryCarSafeCall() = viewModelScope.launch {
        val apiResponse = repository.getEveryCar()
        if(apiResponse.isSuccessful){
            apiResponse.body()?.let {carResponse ->
                carResponse.data?.let { cars ->
                    repository.deleteCachedCars()
                    repository.cacheCars(cars)
                    everyCarState.emit(Resource.Success(cars))
                }
            }
        }else{
            everyCarState.emit(Resource.Error(null, apiResponse.message()))
        }
    }

    fun getCarByType(type: String) = viewModelScope.launch {
        val cars = repository.getCarByTypeDb(type)
        if(cars.isNotEmpty()){
            carByTypeState.emit(Resource.Success(cars))
        }
    }


    fun addACarUploadCache(car: CacheUploadCarModel) = viewModelScope.launch {
        addCarRepository.addACarUploadCache(car)
    }

    fun getAllCacheCarUploads() = addCarRepository.getAllCacheCarUploads()
    fun getSingleCacheCarUpload(id: Int) = addCarRepository.getSingleCacheCarUpload(id = id)
    fun changeCachedUploadCarDetails(
        cachedCarId: String,
        carBrand: String,
        carModel: String,
        carType: String
    ) = viewModelScope.launch {
        addCarRepository.changeCachedUploadCarDetails(cachedCarId, carBrand, carModel, carType)
    }

    fun changeCachedUploadCarPrice(cachedCarId: String,price: String) = viewModelScope.launch {
        addCarRepository.changeCachedUploadCarPrice(cachedCarId, price)
    }
    fun changeCachedUploadCarLocation(cachedCarId: String,lat: String,lng: String) = viewModelScope.launch {
        addCarRepository.changeCachedUploadCarLocation(cachedCarId, lat, lng)
    }
    fun addCarImages(carImage: CachedCarImages) {
        viewModelScope.launch {
            dbService.addCarImages(carImage)
        }
    }

    fun getAllCachedCarImagesByCarId(carId: Int): LiveData<List<CachedCarImages>> {
        return dbService.getAllCachedCarImagesByCarId(carId)
    }












}

