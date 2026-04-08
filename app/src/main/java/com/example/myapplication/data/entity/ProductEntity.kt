package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa la tabla "products" en la base de datos Room.
 * Almacena la información descriptiva de cada producto del catálogo.
 * Cada instancia corresponde a una fila en dicha tabla.
 */
@Entity(tableName = "products")
data class ProductEntity(

    /**
     * Identificador único del producto.
     * Generado automáticamente de forma incremental por Room.
     * No es necesario asignarlo al crear un producto.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    /**
     * Código de barras del producto (EAN13, QR, etc.).
     * Sirve para identificar el producto físicamente mediante escaneo.
     */
    @ColumnInfo(name = "barcode")
    val barcode: String,

    /**
     * Nombre corto del producto.
     * Se usa para mostrar en listas y búsquedas rápidas.
     */
    @ColumnInfo(name = "name")
    val name: String,

    /**
     * Descripción detallada del producto.
     * Puede incluir características, ingredientes, instrucciones, etc.
     */
    @ColumnInfo(name = "description")
    val description: String,

    /**
     * Categoría a la que pertenece el producto.
     * Ejemplos: "Abarrotes", "Limpieza", "Bebidas".
     */
    @ColumnInfo(name = "category")
    val category: String,

    /**
     * Tipo de unidad en la que se mide o vende el producto.
     * Ejemplos: "Kilogramos", "Litros", "Piezas", "Caja".
     */
    @ColumnInfo(name = "unit_type")
    val unitType: String,

    /**
     * Empresa fabricante del producto.
     * Ejemplos: "Nestlé", "P&G", "Unilever".
     */
    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,

    /**
     * Marca comercial bajo la que se vende el producto.
     * Puede diferir del fabricante. Ejemplo: fabricante "P&G", marca "Ariel".
     */
    @ColumnInfo(name = "brand")
    val brand: String,

    /**
     * Indica si el producto está activo en el catálogo.
     * Valor true = disponible para venta.
     * Valor false = descontinuado, pero conservado en el historial.
     * Por defecto es true al crear un producto nuevo.
     */
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true
)