package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa la tabla "providers" en la base de datos Room.
 * Almacena la información de los proveedores que suministran productos.
 * Cada instancia corresponde a una fila en dicha tabla.
 */
@Entity(tableName = "providers")
data class ProviderEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    /**
     * Razón social o nombre comercial de la empresa proveedora.
     * Ejemplo: "Distribuidora El Mayoreo S.A.S"
     */
    @ColumnInfo(name = "business_name")
    val businessName: String,

    /**
     * Número de Identificación Tributaria (NIT) del proveedor.
     * Sirve para identificar legalmente a la empresa ante la DIAN.
     * Ejemplo: "900123456-1"
     */
    @ColumnInfo(name = "nit")
    val nit: String,

    /**
     * Número de teléfono o celular de contacto del proveedor.
     */
    @ColumnInfo(name = "phone")
    val phone: String,

    /**
     * Dirección física de la empresa proveedora.
     * Ejemplo: "Calle 15 #32-10, Cali, Valle del Cauca"
     */
    @ColumnInfo(name = "address")
    val address: String
)