package com.example.donacionesjava.ws_manager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Cambiar por la IP de la computadora
    private const val BASE_URL = "http://10.27.45.23/WS_Donaciones/"

    // Singleton de Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Singleton de ApiService
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}