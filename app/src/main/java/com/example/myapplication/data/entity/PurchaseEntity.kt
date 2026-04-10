package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

/**
 * Entidad que representa la tabla "purchases" en la base de datos.
 * Almacena el encabezado de cada compra realizada a un proveedor.
 * El proveedor se guarda como texto para evitar
 * restricciones de llave foránea.
 */
@Entity(tableName = "purchases")
data class PurchaseEntity(

    /**
     * Identificador único de la compra.
     * Generado automáticamente por Room.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    /**
     * Nombre del proveedor al que se le realizó la compra.
     * Se guarda como texto para no depender de la tabla providers.
     */
    @ColumnInfo(name = "provider_name")
    val providerName: String = "",

    /**
     * Fecha de la compra en formato timestamp (milisegundos).
     * Se obtiene con System.currentTimeMillis()
     */
    @ColumnInfo(name = "date")
    val date: Long = System.currentTimeMillis(),

    /**
     * Total de la compra calculado como la suma
     * de (precio x cantidad) de cada producto.
     */
    @ColumnInfo(name = "total")
    val total: Double = 0.0,

    /**
     * Notas u observaciones adicionales de la compra.
     * Ejemplo: "Factura #123", "Pago a 30 días"
     */
    @ColumnInfo(name = "notes")
    val notes: String = ""
)