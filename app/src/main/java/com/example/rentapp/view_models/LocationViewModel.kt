package com.example.rentapp.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentapp.R
import com.example.rentapp.models.locations_model.LocationModelRequestParams
import com.example.rentapp.models.locations_model.LocationModelResponseParams
import com.example.rentapp.network.ApiInstance
import com.example.rentapp.repositories.LocationRepositoryImpl
import com.example.rentapp.uitls.Const.userId
import com.example.rentapp.uitls.NetworkConnection
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.math.ln

class LocationViewModel(
    private val app: Application
) : AndroidViewModel(app){

    val apiService = ApiInstance.apiService(app)
    val repository = LocationRepositoryImpl(apiService)

    val nc = NetworkConnection()

    val getAllLocationsState: MutableStateFlow<Resource<LocationModelResponseParams>> =  MutableStateFlow(Resource.Idle())
    val addLocationState: MutableStateFlow<Resource<LocationModelResponseParams>> =  MutableStateFlow(Resource.Idle())
    val deleteLocationState: MutableStateFlow<Resource<LocationModelResponseParams>> =  MutableStateFlow(Resource.Idle())
    val dataStore = UserDataStore(app)

    fun addLocation(lat: String?, lng: String?, name: String, city: String) = viewModelScope.launch {
        var nameReplacer: String? = null
        var cityReplacer: String? = null
        if(lat == null && lng == null){
            addLocationState.emit(Resource.Error(null, "Please add a location"))
            return@launch
        }
        val address = Resources.getAddressFromLatLng(lat!!.toDouble(), lng!!.toDouble(), app.applicationContext)
        if(name == "" && city == ""){
            address?.let {
                nameReplacer = address.countryName
                cityReplacer = address.locality ?: address.subLocality ?: ""
            }
        }
        val uid =  dataStore.read(userId)
        val params = LocationModelRequestParams(
            lat,
            lng,
            nameReplacer ?: name,
            cityReplacer ?: city,
            uid!!
        )
        safeCallAddLocationRequest(params)
    }

    private fun safeCallAddLocationRequest(params: LocationModelRequestParams) = viewModelScope.launch {
        try {
            addLocationState.emit(Resource.Loading())
            val isAvailable = nc.init(app)
            if(isAvailable){
                val response = repository.addLocation(params)
                addLocationState.emit(handleAddLocationRequest(response))
            }else{
                addLocationState.emit(Resource.Internet(app.getString(R.string.no_internet_connection)))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    getAllLocationsState.emit(Resource.Error(null,"IOException"))
                }
                else -> getAllLocationsState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            getAllLocationsState.emit(Resource.Error(null, "Socket-Error"))
        }catch (c: ConnectException){
            getAllLocationsState.emit(Resource.Error(null, "Connect-Exception"))
        }
    }

    private fun handleAddLocationRequest(response: Response<LocationModelResponseParams>): Resource<LocationModelResponseParams> {
        if (response.isSuccessful){
            response.body()?.let {
                if(it.success){
                    return Resource.Success(it)
                }else{
                    return Resource.Error(it, it.message)
                }
            }
        }
        return Resource.Error(null, response.message())
    }

    fun getAllLocations() = viewModelScope.launch {
        try {
            getAllLocationsState.emit(Resource.Loading())
            val isAvailable = nc.init(app)
            if(isAvailable){
                val response = repository.allLocations()
                getAllLocationsState.emit(handleGetAllLocationsRequest(response))
            }else{
                getAllLocationsState.emit(Resource.Internet(app.getString(R.string.no_internet_connection)))
            }

        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    getAllLocationsState.emit(Resource.Error(null,"IOException"))
                }
                else -> getAllLocationsState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            getAllLocationsState.emit(Resource.Error(null, "Socket-Error"))
        }catch (c: ConnectException){
            getAllLocationsState.emit(Resource.Error(null, "Connect-Exception"))
        }
    }

    private fun handleGetAllLocationsRequest(response: Response<LocationModelResponseParams>): Resource<LocationModelResponseParams> {
        if(response.isSuccessful){
            response.body()?.let {
                val isSuccessful = it.success
                if(isSuccessful){
                    return Resource.Success(it)
                }else{
                    return Resource.Error(it, it.message)
                }
            }
        }
        return Resource.Error(null, response.message())
    }

    fun deleteLocation(id: Int) = viewModelScope.launch {
        try {
            deleteLocationState.emit(Resource.Loading())
            val isAvailable = nc.init(app)
            if(isAvailable){
                val response = repository.deleteLocation(id)
                deleteLocationState.emit(handleDeleteLocationRequest(response))
            }else{

            }

        }catch (t: Throwable){
            when(t){
                is IOException -> {
                    deleteLocationState.emit(Resource.Error(null,"IOException"))
                }
                else -> deleteLocationState.emit(Resource.Error(null, t.message.toString()))
            }
        }catch (s: SocketTimeoutException){
            deleteLocationState.emit(Resource.Error(null, "Socket-Error"))
        }catch (c: ConnectException){
            deleteLocationState.emit(Resource.Error(null, "Connect-Exception"))
        }
    }

    private fun handleDeleteLocationRequest(response: Response<LocationModelResponseParams>): Resource<LocationModelResponseParams> {
        if(response.isSuccessful){
            response.body()?.let {
                if(it.success){
                    return Resource.Success(it)
                }else{
                    return Resource.Error(it, it.message)
                }
            }
        }
        return Resource.Error(null, response.message())
    }

}