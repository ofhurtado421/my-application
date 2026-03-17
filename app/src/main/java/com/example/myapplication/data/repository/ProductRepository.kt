package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.ProductEntity
import com.example.myapplication.data.dao.ProductDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de productos.
 * Actúa como intermediario entre el ViewModel y el ProductDao.
 * El ViewModel nunca accede directamente al DAO, siempre pasa por aquí.
 *
 * @param productDao El DAO inyectado para acceder a la tabla products.
 */
class ProductRepository(private val productDao: ProductDao) {

    /**
     * Retorna todos los productos como un Flow reactivo.
     * La UI se actualizará automáticamente cada vez que haya cambios.
     */
    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()

    /**
     * Inserta un nuevo producto en la base de datos.
     * @param product El producto a insertar.
     */
    suspend fun insert(product: ProductEntity) {
        productDao.insert(product)
    }

    /**
     * Actualiza un producto existente en la base de datos.
     * @param product El producto con los datos actualizados.
     */
    suspend fun update(product: ProductEntity) {
        productDao.update(product)
    }

    /**
     * Elimina un producto de la base de datos.
     * @param product El producto a eliminar.
     */
    suspend fun delete(product: ProductEntity) {
        productDao.delete(product)
    }

    /**
     * Busca un producto específico por su ID.
     * @param id El ID del producto a buscar.
     * @return Flow que emite el producto encontrado o null si no existe.
     */
    fun getProductById(id: Int): Flow<ProductEntity?> {
        return productDao.getProductById(id)
    }

    /**
     * Busca un producto por su código de barras.
     * Función clave para el escáner del POS.
     * Retorna null si no existe ningún producto con ese código.
     * @param barcode El código de barras a buscar.
     * @return El ProductEntity encontrado o null si no existe.
     */
    suspend fun getProductByBarcode(barcode: String): ProductEntity? {
        return productDao.getProductByBarcode(barcode)
    }
}