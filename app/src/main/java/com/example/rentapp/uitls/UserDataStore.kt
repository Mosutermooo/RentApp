package com.example.rentapp.uitls

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first

class UserDataStore(context: Context) {


    private var dataStore: DataStore<Preferences> = context.createDataStore("Login_Data")

    suspend fun save(key: String, value: String){
        val dataStoreKay = preferencesKey<String>(key)
        dataStore.edit {
            it[dataStoreKay] = value
        }
    }

    suspend fun read(key: String): String?{
        val dataStoreKay = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKay]
    }
    suspend fun delete(){
        dataStore.edit {
            it.clear()
        }
    }

}