package com.example.rentapp.db

import android.net.Uri
import androidx.room.TypeConverter
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

    @TypeConverter
    fun listToJson(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()




}