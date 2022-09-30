package com.example.rentapp.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.models.Car
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.models.add_car_cache_models.CachedCarImages
import com.example.rentapp.uitls.Const.DB_NAME
import com.example.rentapp.uitls.Const.DB_VERSION


@androidx.room.Database(
    entities = [Car::class, CacheUploadCarModel::class, CachedCarImages::class],
    version = DB_VERSION
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun dbService() : DbService

    companion object {
        @Volatile
        private var instance: Database? = null

        fun database(context: Context): Database{
            return instance ?: synchronized(this){
                val buildDBInstance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration().build()
                instance = buildDBInstance
                buildDBInstance
            }
        }
    }

}