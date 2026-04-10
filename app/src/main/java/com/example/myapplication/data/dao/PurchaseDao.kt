package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.myapplication.data.entity.PurchaseDetailEntity
import com.example.myapplication.data.entity.PurchaseEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para las tablas "purchases" y "purchase_details".
 * Maneja todas las operaciones de compras y sus detalles.
 */
@Dao
interface PurchaseDao {

    // ─── Operaciones de PurchaseEntity (encabezado) ────────

    /**
     * Inserta una nueva compra y retorna el ID generado.
     * El ID se usa inmediatamente para insertar los detalles.
     * @return ID de la compra insertada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: PurchaseEntity): Long

    /**
     * Actualiza una compra existente.
     */
    @Update
    suspend fun updatePurchase(purchase: PurchaseEntity)

    /**
     * Elimina una compra.
     * Los detalles se eliminan automáticamente por CASCADE.
     */
    @Delete
    suspend fun deletePurchase(purchase: PurchaseEntity)

    /**
     * Obtiene todas las compras ordenadas por fecha descendente.
     * La más reciente aparece primero.
     */
    @Query("SELECT * FROM purchases ORDER BY date DESC")
    fun getAllPurchases(): Flow<List<PurchaseEntity>>

    /**
     * Obtiene una compra específica por su ID.
     */
    @Query("SELECT * FROM purchases WHERE id = :id")
    fun getPurchaseById(id: Int): Flow<PurchaseEntity?>

    // ─── Operaciones de PurchaseDetailEntity (detalles) ────

    /**
     * Inserta un producto en el detalle de una compra.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseDetail(detail: PurchaseDetailEntity)

    /**
     * Inserta una lista de productos en el detalle de una compra.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseDetails(details: List<PurchaseDetailEntity>)

    /**
     * Elimina todos los detalles de una compra específica.
     */
    @Query("DELETE FROM purchase_details WHERE purchase_id = :purchaseId")
    suspend fun deletePurchaseDetails(purchaseId: Int)

    /**
     * Obtiene todos los detalles de una compra específica.
     * @param purchaseId ID de la compra.
     * @return Flow con la lista de productos de esa compra.
     */
    @Query("SELECT * FROM purchase_details WHERE purchase_id = :purchaseId")
    fun getPurchaseDetails(purchaseId: Int): Flow<List<PurchaseDetailEntity>>

    // ─── Operación combinada ───────────────────────────────

    /**
     * Inserta una compra completa con todos sus productos
     * en una sola transacción atómica.
     *
     * @Transaction garantiza que si algo falla no queda
     * nada a medias. O se guarda todo o no se guarda nada.
     *
     * @param purchase El encabezado de la compra.
     * @param details Lista de productos de la compra.
     */
    @Transaction
    suspend fun insertPurchaseWithDetails(
        purchase: PurchaseEntity,
        details: List<PurchaseDetailEntity>
    ) {
        // Paso 1: inserta el encabezado y obtiene el ID generado
        val purchaseId = insertPurchase(purchase)

        // Paso 2: asigna el purchaseId a cada detalle y los inserta
        val detailsWithId = details.map { it.copy(purchaseId = purchaseId.toInt()) }
        insertPurchaseDetails(detailsWithId)
    }
}