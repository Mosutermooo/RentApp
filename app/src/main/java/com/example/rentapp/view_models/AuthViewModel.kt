package com.example.rentapp.view_models

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.LoginParams
import com.example.models.LoginResponse
import com.example.models.RegisterResponse
import com.example.models.RegisterUserParams
import com.example.rentapp.R
import com.example.rentapp.network.ApiInstance
import com.example.rentapp.network.SeasonManager
import com.example.rentapp.repositories.AuthRepositoryImpl
import com.example.rentapp.repositories.AuthRepositoryTestImpl
import com.example.rentapp.uitls.Const
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class AuthViewModel(
   private var app: Application
)  : AndroidViewModel(app){

    val signUpUserState : MutableStateFlow<Resource<RegisterResponse>> = MutableStateFlow(Resource.Idle())
    val loginUserState : MutableStateFlow<Resource<LoginResponse>> = MutableStateFlow(Resource.Idle())
    val repository = AuthRepositoryImpl(ApiInstance.apiService(app))
    val testRepository = AuthRepositoryTestImpl()
    val dataStore = UserDataStore(app)
    val seasonManager = SeasonManager(app)

    fun signUpUser(
        email: String,
        username: String,
        password: String
    ) = viewModelScope.launch {

        if(email == ""){
            signUpUserState.emit(Resource.Error(null, app.getString(R.string.email_field_empty)))
            return@launch
        }

        if(username == ""){
            signUpUserState.emit(Resource.Error(null, app.getString(R.string.username_field_empty)))
            return@launch
        }

        if(password == ""){
            signUpUserState.emit(Resource.Error(null, app.getString(R.string.password_empty)))
            return@launch
        }

        if(password.length < 5){
            signUpUserState.emit(Resource.Error(null, app.getString(R.string.password_too_small)))
            return@launch
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signUpUserState.emit(Resource.Error(null, app.getString(R.string.invalid_email)))
            return@launch
        }

        val params = RegisterUserParams(
            email,username, password
        )
        safeCallSignUpUser(params)


    }

    private fun safeCallSignUpUser(params: RegisterUserParams) = viewModelScope.launch {
        try {
            signUpUserState.emit(Resource.Loading())
            val response = repository.signUpUser(params)
            signUpUserState.emit(handleSignUpUserCall(response))
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    signUpUserState.emit(Resource.Error(null,"IOException"))
                }
                else -> signUpUserState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            signUpUserState.emit(Resource.Error(null, "Socket-Error"))
        }
    }

    private fun handleSignUpUserCall(response: Response<RegisterResponse>): Resource<RegisterResponse> {
        if(response.isSuccessful){
            response.body()?.let {
                return if(it.success){
                    Resource.Success(it)
                }else{
                    Resource.Error(null, it.message)
                }
            }
        }
        return Resource.Error(null, response.message())
    }

    fun loginUser(username: String, password: String) = viewModelScope.launch {
        if(username == ""){
            loginUserState.emit(Resource.Error(null, app.getString(R.string.username_field_empty)))
            return@launch
        }

        if(password == ""){
            loginUserState.emit(Resource.Error(null, app.getString(R.string.password_empty)))
            return@launch
        }
        val params = LoginParams(
            username, password
        )
        safeCallLoginUser(params)

    }

    private fun safeCallLoginUser(params: LoginParams) = viewModelScope.launch {
        try {
            loginUserState.emit(Resource.Loading())
            val response = repository.loginUser(params)
            loginUserState.emit(handleLoginUserCall(response, params))
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    loginUserState.emit(Resource.Error(null,"IOException"))
                }
                else -> loginUserState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            loginUserState.emit(Resource.Error(null, "Socket-Error"))
        }
    }

    private fun handleLoginUserCall(response: Response<LoginResponse>, params: LoginParams): Resource<LoginResponse> {
        if(response.isSuccessful){
            response.body()?.let {
                return if(it.success){
                    viewModelScope.launch {
                        dataStore.save(Const.username, params.username)
                        dataStore.save(Const.password, params.password)
                        dataStore.save(Const.userId, it.userId!!)
                        dataStore.save(Const.role, it.role)
                        seasonManager.saveToken(it.token!!)
                    }
                    Resource.Success(it)
                }else{
                    Resource.Error(null, it.message)
                }
            }
        }
        return Resource.Error(null, response.message())
    }


}





