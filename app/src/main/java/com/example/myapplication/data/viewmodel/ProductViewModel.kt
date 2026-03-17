package com.example.myapplication.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.ProductEntity
import com.example.myapplication.data.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de productos.
 * Actúa como intermediario entre el Repository y la UI (pantallas).
 * Sobrevive a los cambios de configuración como rotar la pantalla,
 * evitando que los datos se pierdan o se recarguen innecesariamente.
 *
 * @param repository El repositorio inyectado para acceder a los datos.
 */
class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    /**
     * Lista de todos los productos expuesta a la UI como StateFlow.
     * StateFlow siempre tiene un valor actual (empieza con lista vacía).
     * La UI observa este Flow y se actualiza automáticamente cuando cambia.
     */
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Inserta un nuevo producto en la base de datos.
     * Se ejecuta en viewModelScope para no bloquear el hilo principal.
     * @param product El producto a insertar.
     */
    fun insert(product: ProductEntity) {
        viewModelScope.launch {
            repository.insert(product)
        }
    }

    /**
     * Actualiza un producto existente en la base de datos.
     * @param product El producto con los datos actualizados.
     */
    fun update(product: ProductEntity) {
        viewModelScope.launch {
            repository.update(product)
        }
    }

    /**
     * Elimina un producto de la base de datos.
     * @param product El producto a eliminar.
     */
    fun delete(product: ProductEntity) {
        viewModelScope.launch {
            repository.delete(product)
        }
    }

    /**
     * Busca un producto específico por su ID.
     * @param id El ID del producto a buscar.
     * @return StateFlow que emite el producto encontrado o null si no existe.
     */
    fun getProductById(id: Int): StateFlow<ProductEntity?> {
        return repository.getProductById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }

    /**
     * Busca un producto por su código de barras.
     * Función clave para el escáner del POS.
     * Se ejecuta en viewModelScope y actualiza un estado separado.
     * @param barcode El código de barras a buscar.
     * @param onResult Callback que retorna el producto encontrado o null.
     */
    fun getProductByBarcode(barcode: String, onResult: (ProductEntity?) -> Unit) {
        viewModelScope.launch {
            val product = repository.getProductByBarcode(barcode)
            onResult(product)
        }
    }
}