package com.ivanfernandez.userauthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val apiService = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isLoading by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }
            var loginErrorMessage by remember { mutableStateOf<String?>(null) }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }
            var lastName by remember { mutableStateOf("") }
            var isRegistering by remember { mutableStateOf(false) }

            val scope = rememberCoroutineScope()

            // Función de login
            fun loginUser() {
                scope.launch {
                    try {
                        isLoading = true
                        val credentials = LoginCredentials(email = email, password = password)
                        val response = apiService.loginUser(credentials)
                        if (response.isSuccessful) {
                            val user = response.body()  // Obtener el usuario desde la respuesta
                            if (user != null) {
                                // Aquí puedes manejar la lógica después de un login exitoso
                                loginErrorMessage = null
                                // Redirigir o mostrar información del usuario
                            }
                        } else {
                            loginErrorMessage = "Error al iniciar sesión: ${response.message()}"
                        }
                    } catch (e: Exception) {
                        loginErrorMessage = "Error en la conexión: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            }

            // Función de registro
            fun registerUser() {
                scope.launch {
                    try {
                        isLoading = true
                        val user = User(name = name, lastName = lastName, email = email, password = password)
                        val response = apiService.registerUser(user)
                        if (response.isSuccessful) {
                            val newUser = response.body()  // Obtener el usuario registrado desde la respuesta
                            if (newUser != null) {
                                // Lógica adicional después de registrar al usuario, si es necesario
                            }
                        } else {
                            errorMessage = "Error al registrar: ${response.message()}"
                        }
                    } catch (e: Exception) {
                        loginErrorMessage = "Error en la conexión: ${e.localizedMessage}"
                        e.printStackTrace()  // Imprime la excepción completa en Logcat
                    } finally {
                        isLoading = false
                    }
                }
            }

            // Interfaz de login y registro
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    CircularProgressIndicator()  // Indicador de carga
                } else {
                    // Muestra errores de login si los hay
                    if (loginErrorMessage != null) {
                        Text(text = loginErrorMessage ?: "Error", color = Color.Red)
                    }

                    // Si estamos registrando, mostramos el formulario de registro
                    if (isRegistering) {
                        // Formulario de registro
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre") }
                        )
                        TextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Apellido") }
                        )
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo Electrónico") }
                        )
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Button(onClick = { registerUser() }) {
                            Text("Registrar")
                        }
                    } else {
                        // Formulario de login
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo Electrónico") }
                        )
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Button(onClick = { loginUser() }) {
                            Text("Iniciar sesión")
                        }

                        // Enlace para cambiar a registro
                        TextButton(onClick = { isRegistering = true }) {
                            Text("¿No tienes cuenta? Regístrate")
                        }
                    }

                    // Muestra errores de registro si los hay
                    if (errorMessage != null) {
                        Text(text = errorMessage ?: "Error", color = Color.Red)
                    }
                }
            }
        }
    }
}

