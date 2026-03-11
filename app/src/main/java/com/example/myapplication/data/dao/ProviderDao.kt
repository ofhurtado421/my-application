package com.example.myapplication.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.ProviderEntity
import kotlinx.coroutines.flow.Flow


interface ProviderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(provider: ProviderEntity)

    @Update
    suspend fun update(provider: ProviderEntity)

    @Delete
    suspend fun delete(provider: ProviderEntity)

    // Obtenemos una LISTA de proveedores
    @Query("SELECT * FROM providers ORDER BY businessName ASC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers WHERE id = :id")
    fun getProviderById(id: Int): Flow<ProviderEntity?>
}