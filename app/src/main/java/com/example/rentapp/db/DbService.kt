package com.example.rentapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.models.Car

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







}