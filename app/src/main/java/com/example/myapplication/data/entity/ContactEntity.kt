package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa la tabla "contacts" en la base de datos Room.
 * Cada instancia de esta clase corresponde a una fila en dicha tabla.
 */
@Entity(tableName = "contacts")
data class ContactEntity(

    /**
     * Identificador único del contacto.
     * Se genera automáticamente de forma incremental (autoGenerate = true),
     * por lo que no es necesario asignarlo manualmente al crear un contacto.
     * El valor por defecto es 0, Room lo reemplaza al insertar.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Nombre completo del contacto.
     */
    @ColumnInfo(name = "name")
    val name: String,

    /**
     * Número de teléfono del contacto.
     */
    @ColumnInfo(name = "phone")
    val phone: String

    /**
     * @ColumnInfo, es buena práctica para tener control explícito del nombre de cada columna en la tabla.
     */
)