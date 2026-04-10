package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.PurchaseDao
import com.example.myapplication.data.entity.PurchaseDetailEntity
import com.example.myapplication.data.entity.PurchaseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de compras.
 * Actúa como intermediario entre el ViewModel y el PurchaseDao.
 * El ViewModel nunca accede directamente al DAO.
 *
 * @param purchaseDao El DAO inyectado para acceder a las tablas
 *                    purchases y purchase_details.
 */
class PurchaseRepository(private val purchaseDao: PurchaseDao) {

    /**
     * Retorna todas las compras como un Flow reactivo.
     * Ordenadas de la más reciente a la más antigua.
     * La UI se actualiza automáticamente cuando hay cambios.
     */
    val allPurchases: Flow<List<PurchaseEntity>> = purchaseDao.getAllPurchases()

    /**
     * Inserta una compra completa con todos sus productos
     * en una sola transacción atómica.
     * @param purchase El encabezado de la compra.
     * @param details Lista de productos de la compra.
     */
    suspend fun insertPurchaseWithDetails(
        purchase: PurchaseEntity,
        details: List<PurchaseDetailEntity>
    ) {
        purchaseDao.insertPurchaseWithDetails(purchase, details)
    }

    /**
     * Actualiza el encabezado de una compra existente.
     * @param purchase La compra con los datos actualizados.
     */
    suspend fun updatePurchase(purchase: PurchaseEntity) {
        purchaseDao.updatePurchase(purchase)
    }

    /**
     * Elimina una compra y sus detalles automáticamente
     * gracias al CASCADE definido en PurchaseDetailEntity.
     * @param purchase La compra a eliminar.
     */
    suspend fun deletePurchase(purchase: PurchaseEntity) {
        purchaseDao.deletePurchase(purchase)
    }

    /**
     * Busca una compra específica por su ID.
     * @param id El ID de la compra a buscar.
     * @return Flow que emite la compra o null si no existe.
     */
    fun getPurchaseById(id: Int): Flow<PurchaseEntity?> {
        return purchaseDao.getPurchaseById(id)
    }

    /**
     * Obtiene todos los productos de una compra específica.
     * @param purchaseId El ID de la compra.
     * @return Flow con la lista de productos de esa compra.
     */
    fun getPurchaseDetails(purchaseId: Int): Flow<List<PurchaseDetailEntity>> {
        return purchaseDao.getPurchaseDetails(purchaseId)
    }

    /**
     * Elimina todos los detalles de una compra.
     * Útil para actualizar los productos de una compra existente.
     * @param purchaseId El ID de la compra.
     */
    suspend fun deletePurchaseDetails(purchaseId: Int) {
        purchaseDao.deletePurchaseDetails(purchaseId)
    }
}