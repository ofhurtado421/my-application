package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la tabla "contacts".
 * Define las operaciones disponibles para interactuar con los contactos
 * en la base de datos Room.
 *
 * Las funciones con "suspend" se ejecutan en corrutinas (hilo secundario),
 * evitando bloquear el hilo principal de la UI.
 * Las funciones que retornan Flow emiten actualizaciones automáticamente
 * cada vez que los datos cambian en la tabla.
 */
@Dao
interface ContactDao {

    /**
     * Inserta un nuevo contacto en la tabla.
     * Si ya existe un contacto con el mismo ID, lo reemplaza completo.
     * @param contact El objeto ContactEntity a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity)

    /**
     * Actualiza los datos de un contacto existente en la tabla.
     * Room identifica el registro a actualizar por el ID del objeto.
     * @param contact El objeto ContactEntity con los datos actualizados.
     */
    @Update
    suspend fun update(contact: ContactEntity)

    /**
     * Elimina un contacto de la tabla.
     * Room identifica el registro a eliminar por el ID del objeto.
     * @param contact El objeto ContactEntity a eliminar.
     */
    @Delete
    suspend fun delete(contact: ContactEntity)

    /**
     * Obtiene todos los contactos de la tabla como un flujo reactivo.
     * El Flow emite una nueva lista cada vez que hay cambios en la tabla,
     * permitiendo que la UI se actualice automáticamente.
     * @return Flow que emite la lista actualizada de contactos.
     */
    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<ContactEntity>>

    /**
     * Obtiene un contacto específico por su ID como un flujo reactivo.
     * Emite null si no existe ningún contacto con ese ID.
     * @param id El ID del contacto a buscar.
     * @return Flow que emite el contacto encontrado o null si no existe.
     */
    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getContactById(id: Int): Flow<ContactEntity?>
}