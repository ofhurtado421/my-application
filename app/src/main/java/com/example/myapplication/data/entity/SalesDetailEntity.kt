package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey

/**
 * Entidad que representa la tabla "sale_details".
 * Almacena cada producto incluido en una venta.
 * Es la tabla intermedia entre ventas y productos.
 *
 * Relación:
 * - Muchos detalles pertenecen a una SaleEntity
 * - Cada detalle referencia un ProductEntity
 */
@Entity(
    tableName = "sale_details",
    foreignKeys = [
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"],
            /**
             * onDelete CASCADE elimina automáticamente
             * los detalles si se borra la venta.
             */
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class SaleDetailEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    /**
     * ID de la venta a la que pertenece este detalle.
     * Referencia a SaleEntity.id
     */
    @ColumnInfo(name = "sale_id")
    val saleId: Int,

    /**
     * ID del producto vendido.
     * Referencia a ProductEntity.id
     */
    @ColumnInfo(name = "product_id")
    val productId: Int,

    /**
     * Nombre del producto al momento de la venta.
     * Se guarda para mantener historial aunque
     * el producto cambie de nombre después.
     */
    @ColumnInfo(name = "product_name")
    val productName: String,

    /**
     * Cantidad de unidades vendidas del producto.
     */
    @ColumnInfo(name = "quantity")
    val quantity: Int,

    /**
     * Precio de venta unitario al momento de la venta.
     * Se guarda para mantener historial aunque
     * el precio cambie después.
     */
    @ColumnInfo(name = "unit_price")
    val unitPrice: Double,

    /**
     * Subtotal de este producto = quantity * unitPrice
     */
    @ColumnInfo(name = "subtotal")
    val subtotal: Double
)





