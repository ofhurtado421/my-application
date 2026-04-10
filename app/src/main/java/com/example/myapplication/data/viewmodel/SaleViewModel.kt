package com.example.myapplication.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.SaleDetailEntity
import com.example.myapplication.data.entity.SaleEntity
import com.example.myapplication.data.repository.SaleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de ventas.
 * Actúa como intermediario entre el Repository y la UI.
 * Sobrevive a los cambios de configuración como rotar pantalla.
 *
 * @param repository El repositorio inyectado para acceder a los datos.
 */
class SaleViewModel(private val repository: SaleRepository) : ViewModel() {

    /**
     * Lista de todas las ventas expuesta a la UI como StateFlow.
     * Ordenadas de la más reciente a la más antigua.
     * La UI se actualiza automáticamente cuando hay cambios.
     */
    val allSales: StateFlow<List<SaleEntity>> = repository.allSales
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Inserta una venta completa con todos sus productos
     * en una sola transacción atómica.
     * Se ejecuta en viewModelScope para no bloquear la UI.
     * @param sale El encabezado de la venta.
     * @param details Lista de productos de la venta.
     */
    fun insertSaleWithDetails(
        sale: SaleEntity,
        details: List<SaleDetailEntity>
    ) {
        viewModelScope.launch {
            repository.insertSaleWithDetails(sale, details)
        }
    }

    /**
     * Actualiza el encabezado de una venta existente.
     * @param sale La venta con los datos actualizados.
     */
    fun updateSale(sale: SaleEntity) {
        viewModelScope.launch {
            repository.updateSale(sale)
        }
    }

    /**
     * Elimina una venta y sus detalles automáticamente.
     * @param sale La venta a eliminar.
     */
    fun deleteSale(sale: SaleEntity) {
        viewModelScope.launch {
            repository.deleteSale(sale)
        }
    }

    /**
     * Busca una venta específica por su ID.
     * @param id El ID de la venta a buscar.
     * @return StateFlow que emite la venta o null si no existe.
     */
    fun getSaleById(id: Int): StateFlow<SaleEntity?> {
        return repository.getSaleById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }

    /**
     * Obtiene todos los productos de una venta específica.
     * @param saleId El ID de la venta.
     * @return StateFlow con la lista de productos de esa venta.
     */
    fun getSaleDetails(saleId: Int): StateFlow<List<SaleDetailEntity>> {
        return repository.getSaleDetails(saleId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * Elimina todos los detalles de una venta.
     * Útil para actualizar los productos de una venta existente.
     * @param saleId El ID de la venta.
     */
    fun deleteSaleDetails(saleId: Int) {
        viewModelScope.launch {
            repository.deleteSaleDetails(saleId)
        }
    }
}