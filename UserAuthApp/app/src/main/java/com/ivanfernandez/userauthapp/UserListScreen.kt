package com.ivanfernandez.userauthapp

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@Composable
fun UserListScreen(users: List<User>) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Llamada a la API para obtener los usuarios
    LaunchedEffect(Unit) {
        try {
            // Realizamos la llamada a la API
            val response = RetrofitClient.apiService.getUsers()
            if (response.isSuccessful) {
                // Si la respuesta es exitosa, almacenamos los usuarios
                Log.d("UserListScreen", "Usuarios cargados exitosamente: ${response.body()?.size}")
            } else {
                // Si no fue exitosa, mostramos el error en el log
                errorMessage = "Error al cargar usuarios: ${response.message()}"
                Log.e("UserListScreen", "Error en la respuesta: ${response.message()}")
            }
        } catch (e: Exception) {
            // Si ocurre un error en la conexión, mostramos el error
            errorMessage = "Error en la conexión: ${e.message}"
            Log.e("UserListScreen", "Error en la conexión: ${e.message}")
        }
        isLoading = false
    }

    // Mostrar indicador de carga mientras se obtienen los datos
    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Si hay un error, mostramos el mensaje de error
        errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(16.dp))
        }

        // Mostramos la lista de usuarios si todo está correcto
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(users) { user ->
                UserItem(user = user)
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Nombre: ${user.name} ${user.lastName}")
        Text("Email: ${user.email}")
    }
}
