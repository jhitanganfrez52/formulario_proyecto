package com.ivanfernandez.formulario3

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface ApiService {
    @POST("guardar-producto")
    suspend fun guardarProducto(@Body producto: Producto): Response<Producto>
}

