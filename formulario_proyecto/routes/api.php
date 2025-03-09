<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\ProductoController;

Route::post('/guardar-producto', [ProductoController::class, 'guardarProducto']);



