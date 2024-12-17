package com.example.donacionesjava.ws_manager

import com.example.donacionesjava.domain.Administrador
import com.example.donacionesjava.domain.CreadorContenido
import com.example.donacionesjava.domain.Usuario
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface
ApiService {

    @GET("users.php")
    suspend fun getUsuarios(): ApiResponse<List<Usuario>>

    @GET("admins.php")
    suspend fun getAdmins(): ApiResponse<List<Administrador>>

    @GET("creators.php")
    suspend fun getCreadores(): ApiResponse<List<CreadorContenido>>

    @POST("users.php")
    suspend fun registerUsuario(@Body usuario: Usuario): ApiResponse<String>

    @POST("creators.php")
    suspend fun registerCreador(@Body creador: CreadorContenido): ApiResponse<String>

    @PUT("creators.php")
    suspend fun updateCreador(@Body creador: CreadorContenido): ApiResponse<String>

    @GET("users.php")
    suspend fun getUsuarioById(@Query("idUsuario") id: String): ApiResponse<Usuario>

}