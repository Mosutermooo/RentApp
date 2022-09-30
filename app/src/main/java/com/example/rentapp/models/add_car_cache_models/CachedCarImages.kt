package com.example.rentapp.models.add_car_cache_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Cached_Car_Image_table")
data class CachedCarImages(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val imageUri: String,
    val carId: Int
): Serializable