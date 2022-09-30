package com.example.rentapp.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.LoginParams
import com.example.models.User
import com.example.models.UserResponseParams
import com.example.rentapp.db.Database
import com.example.rentapp.network.ApiInstance
import com.example.rentapp.network.SeasonManager
import com.example.rentapp.repositories.AuthRepositoryImpl
import com.example.rentapp.repositories.AuthRepositoryTestImpl
import com.example.rentapp.repositories.UserRepositoryImpl
import com.example.rentapp.uitls.Const.password
import com.example.rentapp.uitls.Const.role
import com.example.rentapp.uitls.Const.userId
import com.example.rentapp.uitls.Const.username
import com.example.rentapp.uitls.NetworkConnection
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
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
    val seasonManager = SeasonManager(app)
    val authRepository = AuthRepositoryImpl(
        ApiInstance.apiService(app)
    )

    val nc = NetworkConnection().init(app)

    fun getUserData() = viewModelScope.launch {
        val username = dataStore.read(username)
        val password = dataStore.read(password)
        val userId = dataStore.read(userId)
        val role = dataStore.read(role)
        if(username != null && password != null && userId != null && role != null){
            if(nc){
                reAuthUser(username, password, userId, role)
            }else{
                getUserDataState.emit(Resource.Error(null, "Error Not Logged In"))
            }
        }else{
            getUserDataState.emit(Resource.Error(null, "Error Not Logged In"))
        }

    }

    private fun reAuthUser(username: String, password: String, userId: String, role: String)  = viewModelScope.launch {
        getUserDataState.emit(Resource.Loading())
        val params = LoginParams(
            username, password
        )
        val response = authRepository.loginUser(params)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.success){
                    seasonManager.saveToken(it.token.toString())
                    safeGetUserDataCall(
                        username,
                        password,
                        userId,
                        role
                    )
                }
            }
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
        }catch (c: ConnectException){
            getUserDataState.emit(Resource.Error(null, "Connect-Exception"))
        }
    }

    private fun handleUserDataResponse(response: Response<UserResponseParams>): Resource<User> {
        if(response.isSuccessful){
            val user = response.body()?.user
            if(user != null){
                Log.e("in handling user data", "${response.body()?.let { it.user?.username }}")
                return Resource.Success(user)
            }
        }

        return Resource.Error(null, "Error ${response.message()}")
    }


}