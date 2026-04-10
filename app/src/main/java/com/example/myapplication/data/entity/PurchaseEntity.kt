package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey

/**
 * Entidad que representa la tabla "purchases" en la base de datos.
 * Almacena el encabezado de cada compra realizada a un proveedor.
 *
 * Relación:
 * - Una compra pertenece a un ProviderEntity (proveedor)
 * - Una compra tiene muchos PurchaseDetailEntity (productos)
 */
@Entity(
    tableName = "purchases",
    foreignKeys = [
        ForeignKey(
            entity = ProviderEntity::class,
            parentColumns = ["id"],
            childColumns = ["provider_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class PurchaseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    /**
     * ID del proveedor al que se le realizó la compra.
     * Referencia a ProviderEntity.id
     */
    @ColumnInfo(name = "provider_id")
    val providerId: Int,

    /**
     * Fecha de la compra en formato timestamp (milisegundos).
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