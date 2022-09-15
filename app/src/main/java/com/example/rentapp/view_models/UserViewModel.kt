package com.example.rentapp.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.User
import com.example.models.UserResponseParams
import com.example.rentapp.db.Database
import com.example.rentapp.network.ApiInstance
import com.example.rentapp.repositories.UserRepositoryImpl
import com.example.rentapp.uitls.Const.password
import com.example.rentapp.uitls.Const.role
import com.example.rentapp.uitls.Const.userId
import com.example.rentapp.uitls.Const.username
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class UserViewModel(
    app: Application
) : AndroidViewModel(app){

    val getUserDataState : MutableStateFlow<Resource<User>> = MutableStateFlow(Resource.Idle())
    private val dataStore = UserDataStore(app)
    val repository = UserRepositoryImpl(
        ApiInstance.apiService(app),
        Database.database(app).dbService()
    )

    fun getUserData() = viewModelScope.launch {
        val username = dataStore.read(username)
        val password = dataStore.read(password)
        val userId = dataStore.read(userId)
        val role = dataStore.read(role)
        if(username != null && password != null && userId != null && role != null){
            safeGetUserDataCall(
                username,
                password,
                userId,
                role
            )
        }else{
            getUserDataState.emit(Resource.Error(null, "Error Not Logged In"))
        }

    }

    private fun safeGetUserDataCall(
        username: String,
        password: String,
        userId: String,
        role: String
    ) = viewModelScope.launch {
        try {
            getUserDataState.emit(Resource.Loading())
            val response = repository.getUserData(userId)
            getUserDataState.emit(handleUserDataResponse(response))
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    getUserDataState.emit(Resource.Error(null,"IOException"))
                }
                else -> getUserDataState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            getUserDataState.emit(Resource.Error(null, "Socket-Error"))
        }
    }

    private fun handleUserDataResponse(response: Response<UserResponseParams>): Resource<User> {
        if(response.isSuccessful){
            val user = response.body()?.user
            if(user != null){
                return Resource.Success(user)
            }
        }

        return Resource.Error(null, "Error ${response.message()}")
    }


}