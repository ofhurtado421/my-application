package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.myapplication.data.entity.SaleDetailEntity
import com.example.myapplication.data.entity.SaleEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para las tablas "sales" y "sale_details".
 * Maneja todas las operaciones de ventas y sus detalles.
 */
@Dao
interface SaleDao {

    // ─── Operaciones de SaleEntity (encabezado) ────────────

    /**
     * Inserta una nueva venta y retorna el ID generado.
     * El ID se usa inmediatamente para insertar los detalles.
     * @return ID de la venta insertada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity): Long

    /**
     * Actualiza una venta existente.
     */
    @Update
    suspend fun updateSale(sale: SaleEntity)

    /**
     * Elimina una venta.
     * Los detalles se eliminan automáticamente por CASCADE.
     */
    @Delete
    suspend fun deleteSale(sale: SaleEntity)

    /**
     * Obtiene todas las ventas ordenadas por fecha descendente.
     * La más reciente aparece primero.
     */
    @Query("SELECT * FROM sales ORDER BY date DESC")
    fun getAllSales(): Flow<List<SaleEntity>>

    /**
     * Obtiene una venta específica por su ID.
     */
    @Query("SELECT * FROM sales WHERE id = :id")
    fun getSaleById(id: Int): Flow<SaleEntity?>

    // ─── Operaciones de SaleDetailEntity (detalles) ────────

    /**
     * Inserta un producto en el detalle de una venta.
     * @param detail El detalle con saleId, productId, cantidad y precio.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleDetail(detail: SaleDetailEntity)

    /**
     * Inserta una lista de productos en el detalle de una venta.
     * Más eficiente que insertar uno por uno.
     * @param details Lista de detalles a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleDetails(details: List<SaleDetailEntity>)

    /**
     * Elimina todos los detalles de una venta específica.
     * Útil para actualizar los productos de una venta existente.
     * @param saleId ID de la venta cuyos detalles se eliminarán.
     */
    @Query("DELETE FROM sale_details WHERE sale_id = :saleId")
    suspend fun deleteSaleDetails(saleId: Int)

    /**
     * Obtiene todos los detalles de una venta específica.
     * @param saleId ID de la venta.
     * @return Flow con la lista de productos de esa venta.
     */
    @Query("SELECT * FROM sale_details WHERE sale_id = :saleId")
    fun getSaleDetails(saleId: Int): Flow<List<SaleDetailEntity>>

    // ─── Operación combinada ───────────────────────────────

    /**
     * Inserta una venta completa con todos sus productos
     * en una sola transacción atómica.
     *
     * @Transaction garantiza que si algo falla, no queda
     * nada a medias en la base de datos. O se guarda todo
     * o no se guarda nada.
     *
     * @param sale El encabezado de la venta.
     * @param details Lista de productos de la venta.
     */
    @Transaction
    suspend fun insertSaleWithDetails(
        sale: SaleEntity,
        details: List<SaleDetailEntity>
    ) {
        // Paso 1: inserta el encabezado y obtiene el ID generado
        val saleId = insertSale(sale)

        // Paso 2: asigna el saleId a cada detalle y los inserta
        val detailsWithId = details.map { it.copy(saleId = saleId.toInt()) }
        insertSaleDetails(detailsWithId)
    }
}