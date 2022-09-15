package com.example.rentapp.network

import android.content.Context
import com.example.rentapp.uitls.Const.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiInstance {

    private val logging = HttpLoggingInterceptor()


    private fun okHttpClient(context: Context) : OkHttpClient {
        val token = SeasonManager(context).fetchToken()
        return OkHttpClient.Builder().addNetworkInterceptor(object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
                return chain.proceed(request)
            }
        }).addInterceptor(logging).connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build()
    }

    fun apiService(context: Context): ApiService {
        val serviceBuilder = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient(context)).build()
        return serviceBuilder.create(ApiService::class.java)
    }






}