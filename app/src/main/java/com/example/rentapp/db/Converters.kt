package com.example.rentapp.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.models.Car
import com.example.models.CarImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {


    @TypeConverter
    fun fromCarImages(carImages: List<CarImage>?) : String? {
        if(carImages == null){
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<CarImage>>() {}.type
        val json: String = gson.toJson(carImages, type)
        return json
    }

    @TypeConverter
    fun toCarImages(carImageString: String?) : List<CarImage>?{
        if(carImageString == null){
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<CarImage>>() {}.type
        val carImages = gson.fromJson<List<CarImage>>(carImageString, type)
        return carImages

    }




}