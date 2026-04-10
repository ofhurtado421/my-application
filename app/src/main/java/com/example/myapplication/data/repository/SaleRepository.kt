package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.SaleDao
import com.example.myapplication.data.entity.SaleDetailEntity
import com.example.myapplication.data.entity.SaleEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de ventas.
 * Actúa como intermediario entre el ViewModel y el SaleDao.
 * El ViewModel nunca accede directamente al DAO.
 *
 * @param saleDao El DAO inyectado para acceder a las tablas
 *                sales y sale_details.
 */
class SaleRepository(private val saleDao: SaleDao) {

    /**
     * Retorna todas las ventas como un Flow reactivo.
     * Ordenadas de la más reciente a la más antigua.
     * La UI se actualiza automáticamente cuando hay cambios.
     */
    val allSales: Flow<List<SaleEntity>> = saleDao.getAllSales()

    /**
     * Inserta una venta completa con todos sus productos
     * en una sola transacción atómica.
     * @param sale El encabezado de la venta.
     * @param details Lista de productos de la venta.
     */
    suspend fun insertSaleWithDetails(
        sale: SaleEntity,
        details: List<SaleDetailEntity>
    ) {
        saleDao.insertSaleWithDetails(sale, details)
    }

    /**
     * Actualiza el encabezado de una venta existente.
     * @param sale La venta con los datos actualizados.
     */
    suspend fun updateSale(sale: SaleEntity) {
        saleDao.updateSale(sale)
    }

    /**
     * Elimina una venta y sus detalles automáticamente
     * gracias al CASCADE definido en SaleDetailEntity.
     * @param sale La venta a eliminar.
     */
    suspend fun deleteSale(sale: SaleEntity) {
        saleDao.deleteSale(sale)
    }

    /**
     * Busca una venta específica por su ID.
     * @param id El ID de la venta a buscar.
     * @return Flow que emite la venta o null si no existe.
     */
    fun getSaleById(id: Int): Flow<SaleEntity?> {
        return saleDao.getSaleById(id)
    }

    /**
     * Obtiene todos los productos de una venta específica.
     * @param saleId El ID de la venta.
     * @return Flow con la lista de productos de esa venta.
     */
    fun getSaleDetails(saleId: Int): Flow<List<SaleDetailEntity>> {
        return saleDao.getSaleDetails(saleId)
    }

    /**
     * Elimina todos los detalles de una venta.
     * Útil para actualizar los productos de una venta existente.
     * @param saleId El ID de la venta.
     */
    suspend fun deleteSaleDetails(saleId: Int) {
        saleDao.deleteSaleDetails(saleId)
    }
}