package com.example.myapplication.data.repository
import com.example.myapplication.data.dao.ContactDao
import com.example.myapplication.data.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de contactos.
 * Actúa como intermediario entre el ViewModel y el ContactDao.
 * El ViewModel nunca accede directamente al DAO, siempre pasa por aquí.
 *
 * @param contactDao El DAO inyectado para acceder a la tabla contacts.
 */
class ContactRepository(private val contactDao: ContactDao) {

    /**
     * Retorna todos los contactos como un Flow reactivo.
     * La UI se actualizará automáticamente cada vez que haya cambios.
     */
    val allContacts: Flow<List<ContactEntity>> = contactDao.getAllContacts()

    /**
     * Inserta un nuevo contacto en la base de datos.
     * Se ejecuta en un hilo secundario gracias a suspend.
     * @param contact El contacto a insertar.
     */
    suspend fun insert(contact: ContactEntity) {
        contactDao.insert(contact)
    }

    /**
     * Actualiza un contacto existente en la base de datos.
     * Room identifica el registro por el ID del objeto.
     * @param contact El contacto con los datos actualizados.
     */
    suspend fun update(contact: ContactEntity) {
        contactDao.update(contact)
    }

    /**
     * Elimina un contacto de la base de datos.
     * Room identifica el registro por el ID del objeto.
     * @param contact El contacto a eliminar.
     */
    suspend fun delete(contact: ContactEntity) {
        contactDao.delete(contact)
    }

    /**
     * Busca un contacto específico por su ID.
     * @param id El ID del contacto a buscar.
     * @return Flow que emite el contacto encontrado o null si no existe.
     */
    fun getContactById(id: Int): Flow<ContactEntity?> {
        return contactDao.getContactById(id)
    }
}