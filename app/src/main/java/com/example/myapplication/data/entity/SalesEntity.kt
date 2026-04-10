package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

/**
 * Entidad que representa la tabla "sales" en la base de datos.
 * Almacena el encabezado de cada venta realizada.
 * El cliente se guarda como texto para evitar
 * restricciones de llave foránea.
 */
@Entity(tableName = "sales")
data class SaleEntity(

    /**
     * Identificador único de la venta.
     * Generado automáticamente por Room.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    /**
     * Nombre del cliente al que se le realizó la venta.
     * Se guarda como texto para no depender de la tabla contacts.
     */
    @ColumnInfo(name = "client_name")
    val clientName: String = "",

    /**
     * Fecha de la venta en formato timestamp (milisegundos).
     * Se obtiene con System.currentTimeMillis()
     */
    @ColumnInfo(name = "date")
    val date: Long = System.currentTimeMillis(),

    /**
     * Total de la venta calculado como la suma
     * de (precio x cantidad) de cada producto.
     */
    @ColumnInfo(name = "total")
    val total: Double = 0.0,

    /**
     * Notas u observaciones adicionales de la venta.
     * Ejemplo: "Pago con tarjeta", "Entrega a domicilio"
     */
    @ColumnInfo(name = "notes")
    val notes: String = ""
)