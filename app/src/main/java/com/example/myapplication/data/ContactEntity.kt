package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "contacts")


// nombre de la tabla en la base de datos
data class ContactEntity(

    @PrimaryKey(autoGenerate = true)  // es incremental -> autoasignada el id
    val id: Int =0 ,
    val name: String,
    val phone: String


)