package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.ProviderEntity
import com.example.myapplication.data.dao.ProviderDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de proveedores.
 * Actúa como intermediario entre el ViewModel y el ProviderDao.
 * El ViewModel nunca accede directamente al DAO, siempre pasa por aquí.
 *
 * @param providerDao El DAO inyectado para acceder a la tabla providers.
 */
class ProviderRepository(private val providerDao: ProviderDao) {


    val allProviders: Flow<List<ProviderEntity>> = providerDao.getAllProviders()


    suspend fun insert(provider: ProviderEntity) {
        providerDao.insert(provider)
    }


    suspend fun update(provider: ProviderEntity) {
        providerDao.update(provider)
    }

    /**
     * Elimina un proveedor de la base de datos.
     * @param provider El proveedor a eliminar.
     */
    suspend fun delete(provider: ProviderEntity) {
        providerDao.delete(provider)
    }

    /**
     * Busca un proveedor específico por su ID.
     * @param id El ID del proveedor a buscar.
     * @return Flow que emite el proveedor encontrado o null si no existe.
     */
    fun getProviderById(id: Int): Flow<ProviderEntity?> {
        return providerDao.getProviderById(id)
    }
}
