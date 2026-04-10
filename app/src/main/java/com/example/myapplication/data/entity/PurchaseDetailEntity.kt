package com.example.myapplication.data.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey

/**
 * Entidad que representa la tabla "purchase_details".
 * Almacena cada producto incluido en una compra.
 *
 * Relación:
 * - Muchos detalles pertenecen a una PurchaseEntity
 * - Cada detalle referencia un ProductEntity
 */
@Entity(
    tableName = "purchase_details",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchase_id"],
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
data class PurchaseDetailEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    /**
     * ID de la compra a la que pertenece este detalle.
     * Referencia a PurchaseEntity.id
     */
    @ColumnInfo(name = "purchase_id")
    val purchaseId: Int,

    /**
     * ID del producto comprado.
     * Referencia a ProductEntity.id
     */
    @ColumnInfo(name = "product_id")
    val productId: Int,

    /**
     * Nombre del producto al momento de la compra.
     * Se guarda para mantener historial.
     */
    @ColumnInfo(name = "product_name")
    val productName: String,

    /**
     * Cantidad de unidades compradas del producto.
     */
    @ColumnInfo(name = "quantity")
    val quantity: Int,

    /**
     * Precio de compra unitario al momento de la compra.
     * Se guarda para mantener historial.
     */
    @ColumnInfo(name = "unit_price")
    val unitPrice: Double,

    /**
     * Subtotal de este producto = quantity * unitPrice
     */
    @ColumnInfo(name = "subtotal")
    val subtotal: Double
)
