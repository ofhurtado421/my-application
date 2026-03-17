package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.entity.ProviderEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la tabla "providers".
 * Define las operaciones disponibles para interactuar con los proveedores
 * en la base de datos Room.
 *
 * Las funciones con "suspend" se ejecutan en corrutinas (hilo secundario),
 * evitando bloquear el hilo principal de la UI.
 * Las funciones que retornan Flow emiten actualizaciones automáticamente
 * cada vez que los datos cambian en la tabla.
 */
@Dao // ⚠️ Corrección: faltaba la anotación @Dao, sin ella Room no reconoce esta interfaz
interface ProviderDao {

    /**
     * Inserta un nuevo proveedor en la tabla.
     * Si ya existe un proveedor con el mismo ID, lo reemplaza completo.
     * @param provider El objeto ProviderEntity a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(provider: ProviderEntity)

    /**
     * Actualiza los datos de un proveedor existente en la tabla.
     * Room identifica el registro a actualizar por el ID del objeto.
     * @param provider El objeto ProviderEntity con los datos actualizados.
     */
    @Update
    suspend fun update(provider: ProviderEntity)

    /**
     * Elimina un proveedor de la tabla.
     * Room identifica el registro a eliminar por el ID del objeto.
     * @param provider El objeto ProviderEntity a eliminar.
     */
    @Delete
    suspend fun delete(provider: ProviderEntity)

    /**
     * Obtiene todos los proveedores de la tabla ordenados alfabéticamente
     * por nombre comercial (businessName) de forma ascendente (A-Z).
     * El Flow emite una nueva lista cada vez que hay cambios en la tabla.
     * @return Flow que emite la lista actualizada de proveedores.
     */
    @Query("SELECT * FROM providers ORDER BY business_name ASC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    /**
     * Obtiene un proveedor específico por su ID como un flujo reactivo.
     * Emite null si no existe ningún proveedor con ese ID.
     * @param id El ID del proveedor a buscar.
     * @return Flow que emite el proveedor encontrado o null si no existe.
     */
    @Query("SELECT * FROM providers WHERE id = :id")
    fun getProviderById(id: Int): Flow<ProviderEntity?>
}