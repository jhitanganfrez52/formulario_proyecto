package com.ivanfernandez.userauthapp

data class User(
    val id: String? = null,
    val name: String,
    val lastName: String,  // Agregado 'lastName'
    val email: String,
    val password: String  // Asegúrate de que 'password' esté en la clase
)

