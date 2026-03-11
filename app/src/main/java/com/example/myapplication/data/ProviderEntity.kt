package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "providers")

class ProviderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val businessName: String, // Nombre de la empresa
    val nit: String,
    val phone: String,
    val address: String
)
