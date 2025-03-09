<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Producto;

class ProductoController extends Controller
{
    public function guardarProducto(Request $request)
    {
        // Validamos los datos
        $validatedData = $request->validate([
            'nombre' => 'required|string|max:255',
            'precio' => 'required|numeric',
            'descripcion' => 'required|string',
        ]);

        // Creamos el producto
        $producto = Producto::create([
            'nombre' => $validatedData['nombre'],
            'precio' => $validatedData['precio'],
            'descripcion' => $validatedData['descripcion'],
        ]);

        // Respondemos con un mensaje de éxito y el producto creado
        return response()->json([
            'mensaje' => 'Producto guardado correctamente',
            'producto' => $producto,
        ], 201);  // Código 201: recurso creado exitosamente
    }
}

