package com.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "car_table")
data class Car(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int,
    val id: Int? = null,
    val car_Brand: String,
    val car_Model: String,
    val car_Type: String,
    val totalPrice: Int,
    val status: String,
    val carImage: List<CarImage>
): Serializable
