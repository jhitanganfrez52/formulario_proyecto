package com.ivanfernandez.formulario3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material3.TextField
import java.security.cert.CertificateException
import javax.net.ssl.SSLHandshakeException
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormularioScreen()
        }
    }
}

@Composable
fun FormularioScreen() {
    val (nombre, setNombre) = remember { mutableStateOf("") }
    val (precio, setPrecio) = remember { mutableStateOf("") }
    val (descripcion, setDescripcion) = remember { mutableStateOf("") }
    val mensaje = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Formulario de Producto", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre del Producto
        TextField(
            value = nombre,
            onValueChange = setNombre,
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Precio
        TextField(
            value = precio,
            onValueChange = setPrecio,
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        TextField(
            value = descripcion,
            onValueChange = setDescripcion,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para guardar
        Button(onClick = {
            if (precio.isEmpty() || nombre.isEmpty() || descripcion.isEmpty()) {
                mensaje.value = "Todos los campos son obligatorios."
            } else if (precio.toDoubleOrNull() == null) {
                mensaje.value = "El precio debe ser un número válido."
            } else {
                val producto = Producto(nombre, precio.toDouble(), descripcion)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Realizamos la llamada a la API
                        val response: Response<Producto> = RetrofitInstance.api.guardarProducto(producto)

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                mensaje.value = "Producto guardado con éxito"
                            } else {
                                val errorBody = response.errorBody()?.string()
                                mensaje.value = "Error al guardar el producto: $errorBody"
                            }
                        }
                    } catch (e: SSLHandshakeException) {
                        // Captura errores SSL
                        Log.e("SSLError", "Error SSL: Certificado no válido", e)
                        withContext(Dispatchers.Main) {
                            mensaje.value = "Problema con la conexión SSL"
                        }
                    } catch (e: Exception) {
                        // Captura cualquier otro tipo de error
                        Log.e("Error", "Error al conectar: ${e.message}", e)
                        withContext(Dispatchers.Main) {
                            mensaje.value = "Error al guardar producto"
                        }
                    }
                }
            }
        }) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(mensaje.value, color = Color.Red)
    }
}
