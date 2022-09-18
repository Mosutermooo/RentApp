package com.example.rentapp.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.Car
import com.example.models.RentCarRequestParams
import com.example.models.RentCarResponseParams
import com.example.rentapp.network.ApiInstance
import com.example.rentapp.repositories.CarRepositoryImpl
import com.example.rentapp.repositories.RentRepositoryImpl
import com.example.rentapp.uitls.Const
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class RentViewModel(private var app: Application) : AndroidViewModel(app) {

    private val apiService = ApiInstance.apiService(app)
    private val repository = RentRepositoryImpl(apiService)
    val rentState : MutableStateFlow<Resource<RentCarResponseParams>> = MutableStateFlow(Resource.Idle())
    val dataStore = UserDataStore(app)



    fun getCarStatus(params: RentCarRequestParams) = viewModelScope.launch {
        rentState.emit(Resource.Loading())
        val statusResponse = repository.getCarStatus(params.carId)
        if(statusResponse.isSuccessful) {
            statusResponse.body()?.let {status ->
                if(status == "available"){
                    rentCar(params)
                }else{
                    rentState.emit(Resource.Error(null, "The Car is unavailable"))
                }
            }
        }
    }

    private fun rentCar(params: RentCarRequestParams) = viewModelScope.launch {
        try {
            val rentResponse = repository.rentACar(params)
            rentState.emit(handleRentResponse(rentResponse))
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    rentState.emit(Resource.Error(null,"IOException"))
                }
                else -> rentState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            rentState.emit(Resource.Error(null, "Socket-Error"))
        }

    }

    private fun handleRentResponse(response: Response<RentCarResponseParams>): Resource<RentCarResponseParams> {
        if(response.isSuccessful){
            response.body()?.let {
                if(it.success){
                    return Resource.Success(it)
                }else{
                    return Resource.Error(null, it.message)
                }

            }
        }
        return Resource.Error(null, response.message())
    }


}