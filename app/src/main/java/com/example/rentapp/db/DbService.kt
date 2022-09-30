package com.example.rentapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.models.Car
import com.example.models.CarImage
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.models.add_car_cache_models.CachedCarImages

@Dao
interface DbService {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheCars(cars: List<Car>)

    @Query("select * from car_table")
    fun getCachedCars() : LiveData<List<Car>>

    @Query("delete from car_table")
    suspend fun deleteCachedCars()

    @Query("Select * from car_table where car_Type = :type")
    suspend fun getCarByType(type: String): List<Car>


    //Add Car Queries
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = CacheUploadCarModel::class)
    suspend fun addACarUploadCache(car: CacheUploadCarModel)

    @Query("select * from add_a_car")
    fun getAllCacheCarUploads() : LiveData<List<CacheUploadCarModel>>

    @Query("select * from add_a_car where id = :id")
    fun getSingleCacheCarUpload(id: Int): LiveData<CacheUploadCarModel>

    @Query("update add_a_car set carBrand=:carBrand, carModel=:carModel, carType=:carType WHERE id = :cachedCarId")
    suspend fun changeCachedUploadCarDetails(
        cachedCarId: String,
        carBrand: String,
        carModel: String,
        carType: String
    )

    @Query("update add_a_car set car_Price=:carPrice where id=:cachedCarId")
    suspend fun changeCachedUploadCarPrice(cachedCarId: String,carPrice: String)

    @Query("update add_a_car set carLocationLat=:lat, carLocationLng=:lng where id=:cachedCarId")
    suspend fun changeCachedUploadCarLocation(
        cachedCarId: String,
        lat: String,
        lng: String
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCarImages(carImage: CachedCarImages)

    @Query("SELECT * from cached_car_image_table where carId=:carId")
    fun getAllCachedCarImagesByCarId(carId: Int) : LiveData<List<CachedCarImages>>











}