package com.example.rentapp.models.add_car_cache_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "add_a_car")
data class CacheUploadCarModel(
    @PrimaryKey(autoGenerate = true)
    val primaryId: Int? = null,
    var id: Int,
    var carBrand: String,
    var carModel: String,
    var carType: String,
    var car_Price: Int,
    var carLocationLat: String,
    var carLocationLng: String
): java.io.Serializable{
    constructor() : this(null,0, "", "", "", 0, "", "")
}