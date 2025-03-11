package com.ivanfernandez.userauthapp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun registerUser(@Body user: User): Response<User>  // Cambié Response<ResponseBody> por Response<User>

    @POST("login")
    suspend fun loginUser(@Body credentials: LoginCredentials): Response<User>

    @GET("usuarios")  // Cambié "users" por "usuarios"
    suspend fun getUsers(): Response<List<User>>
}

data class LoginCredentials(
    val email: String,
    val password: String
)
