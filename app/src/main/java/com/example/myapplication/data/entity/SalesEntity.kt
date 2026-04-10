package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey

/**
 * Entidad que representa la tabla "sales" en la base de datos.
 * Almacena el encabezado de cada venta realizada.
 * Cada venta está asociada a un contacto (cliente).
 *
 * Relación:
 * - Una venta pertenece a un ContactEntity (cliente)
 * - Una venta tiene muchos SaleDetailEntity (productos)
 */
@Entity(
    tableName = "sales",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contact_id"],
            /**
             * onDelete RESTRICT evita borrar un cliente
             * si tiene ventas asociadas.
             */
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class SaleEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    /**
     * ID del cliente al que se le realizó la venta.
     * Referencia a ContactEntity.id
     */
    @ColumnInfo(name = "contact_id")
    val contactId: Int,

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