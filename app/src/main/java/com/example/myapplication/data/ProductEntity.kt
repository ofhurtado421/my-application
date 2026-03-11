package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("products")

data class ProductEntity (


    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val barcode: String,      // Código de barras (EAN13, QR, etc.)
    val name: String,         // Nombre corto del producto
    val description: String,  // Descripción detallada
    val category: String,     // Ejemplo: "Abarrotes", "Limpieza"

    val unitType: String,     // Ejemplo: "Kilogramos", "Litros", "Piezas", "Caja"
    val manufacturer: String, // La empresa que lo fabrica (ej. "Nestlé", "P&G")
    val brand: String,        // La marca comercial

    val isActive: Boolean = true // Por si dejas de venderlo pero no quieres borrarlo
)






