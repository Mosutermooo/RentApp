package com.example.rentapp.uitls

sealed class Resource<T> (val data: T? = null, val message: String? =  null) {

    class Idle<T>(): Resource<T>()
    class Loading<T>(): Resource<T>()
    class Success<T>(data : T) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String?) : Resource<T>(data, message)

}