package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la tabla "products".
 * Define las operaciones disponibles para interactuar con los productos
 * en la base de datos Room.
 *
 * Las funciones con "suspend" se ejecutan en corrutinas (hilo secundario),
 * evitando bloquear el hilo principal de la UI.
 * Las funciones que retornan Flow emiten actualizaciones automáticamente
 * cada vez que los datos cambian en la tabla.
 */
@Dao
interface ProductDao {

    /**
     * Inserta un nuevo producto en la tabla.
     * Si ya existe un producto con el mismo ID, lo reemplaza completo.
     * @param product El objeto ProductEntity a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    /**
     * Actualiza los datos de un producto existente en la tabla.
     * Room identifica el registro a actualizar por el ID del objeto.
     * @param product El objeto ProductEntity con los datos actualizados.
     */
    @Update
    suspend fun update(product: ProductEntity)

    /**
     * Elimina un producto de la tabla.
     * Room identifica el registro a eliminar por el ID del objeto.
     * @param product El objeto ProductEntity a eliminar.
     */
    @Delete
    suspend fun delete(product: ProductEntity)

    /**
     * Obtiene todos los productos de la tabla como un flujo reactivo.
     * El Flow emite una nueva lista cada vez que hay cambios en la tabla,
     * permitiendo que la UI se actualice automáticamente.
     * @return Flow que emite la lista actualizada de productos.
     */
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    /**
     * Obtiene un producto específico por su ID como un flujo reactivo.
     * Emite null si no existe ningún producto con ese ID.
     * @param id El ID del producto a buscar.
     * @return Flow que emite el producto encontrado o null si no existe.
     */
    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): Flow<ProductEntity?>

    /**
     * Busca un producto por su código de barras.
     * Función clave para el escáner del POS: cuando se escanea un producto,
     * se busca directamente por su barcode para obtener sus datos al instante.
     * LIMIT 1 garantiza que solo retorna un resultado aunque haya duplicados.
     * Retorna null si no existe ningún producto con ese código de barras.
     * @param barcode El código de barras a buscar (EAN13, QR, etc.).
     * @return El ProductEntity encontrado o null si no existe.
     */
    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?
}