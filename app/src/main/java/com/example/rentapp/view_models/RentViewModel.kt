package com.example.rentapp.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.models.AllRentsResponseParams
import com.example.models.RentCarRequestParams
import com.example.models.RentCarResponseParams
import com.example.models.RentsResponseParams
import com.example.rentapp.R
import com.example.rentapp.network.ApiInstance
import com.example.rentapp.repositories.RentRepositoryImpl
import com.example.rentapp.uitls.Const
import com.example.rentapp.uitls.NetworkConnection
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RentViewModel(private var app: Application) : AndroidViewModel(app) {

    private val apiService = ApiInstance.apiService(app)
    private val repository = RentRepositoryImpl(apiService)
    val rentState : MutableStateFlow<Resource<RentCarResponseParams>> = MutableStateFlow(Resource.Idle())
    val userRentsState : MutableStateFlow<Resource<RentsResponseParams>> = MutableStateFlow(Resource.Idle())
    val rentsByRentIdState : MutableStateFlow<Resource<RentsResponseParams>> = MutableStateFlow(Resource.Idle())
    val dataStore = UserDataStore(app)
    val nc = NetworkConnection()



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
        }catch (c: ConnectException){
            rentState.emit(Resource.Error(null, "Connect-Exception"))
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


    fun getUserRents() = viewModelScope.launch {
        val userId = dataStore.read(Const.userId)
        if(userId == null) userRentsState.emit(Resource.Error(null, app.getString(R.string.log_in_to_see_rent)))
        try {
            userRentsState.emit(Resource.Loading())
            if(nc.init(app)){
                val response = apiService.getUserRents(userId!!)
                userRentsState.emit(handleUserRentsResponse(response))
            }else{
                userRentsState.emit(Resource.Internet(app.getString(R.string.no_internet_connection)))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    userRentsState.emit(Resource.Error(null,"IOException"))
                }
                else -> userRentsState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            userRentsState.emit(Resource.Error(null, "Socket-Error"))
        }catch (c: ConnectException){
            userRentsState.emit(Resource.Error(null, "Connect-Exception"))
        }

    }

    private fun handleUserRentsResponse(response: Response<RentsResponseParams>): Resource<RentsResponseParams> {
        if(response.isSuccessful){
            val data = response.body()
            data ?: return  Resource.Error(data, "No rents so far")
            return if(data.success){
                Resource.Success(data)
            }else{
                Resource.Error(data, data.message)
            }
        }
        return Resource.Error(null, response.message())
    }

    fun getRentsByRentId(rentId: String) = viewModelScope.launch {
        try {
            rentsByRentIdState.emit(Resource.Loading())
            if(nc.init(app)){
                val response = repository.getRentsByRentId(rentId)
                val handledResponse: Resource<RentsResponseParams> = handleRentsByRentIdResponse(response)
                rentsByRentIdState.emit(handledResponse)
            }else{
                rentsByRentIdState.emit(Resource.Internet(app.getString(R.string.no_internet_connection)))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    rentsByRentIdState.emit(Resource.Error(null,"IOException"))
                }
                else -> rentsByRentIdState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            rentsByRentIdState.emit(Resource.Error(null, "Socket-Error"))
        }catch (c: ConnectException){
            rentsByRentIdState.emit(Resource.Error(null, "Connect-Exception"))
        }
    }

    private fun handleRentsByRentIdResponse(response: Response<RentsResponseParams>): Resource<RentsResponseParams> {
        if(response.isSuccessful){
            val body = response.body()
            return if(body != null){
                if(body.success){
                    Resource.Success(body)
                }else{
                    Resource.Error(body, body.message)
                }
            }else{
                Resource.Error(body, "There is no data")
            }
        }
        return Resource.Error(null, response.message())
    }


}