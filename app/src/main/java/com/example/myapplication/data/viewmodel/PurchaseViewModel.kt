package com.example.myapplication.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.PurchaseDetailEntity
import com.example.myapplication.data.entity.PurchaseEntity
import com.example.myapplication.data.repository.PurchaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de compras.
 * Actúa como intermediario entre el Repository y la UI.
 * Sobrevive a los cambios de configuración como rotar pantalla.
 *
 * @param repository El repositorio inyectado para acceder a los datos.
 */
class PurchaseViewModel(private val repository: PurchaseRepository) : ViewModel() {

    /**
     * Lista de todas las compras expuesta a la UI como StateFlow.
     * Ordenadas de la más reciente a la más antigua.
     * La UI se actualiza automáticamente cuando hay cambios.
     */
    val allPurchases: StateFlow<List<PurchaseEntity>> = repository.allPurchases
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Inserta una compra completa con todos sus productos
     * en una sola transacción atómica.
     * Se ejecuta en viewModelScope para no bloquear la UI.
     * @param purchase El encabezado de la compra.
     * @param details Lista de productos de la compra.
     */
    fun insertPurchaseWithDetails(
        purchase: PurchaseEntity,
        details: List<PurchaseDetailEntity>
    ) {
        viewModelScope.launch {
            repository.insertPurchaseWithDetails(purchase, details)
        }
    }

    /**
     * Actualiza el encabezado de una compra existente.
     * @param purchase La compra con los datos actualizados.
     */
    fun updatePurchase(purchase: PurchaseEntity) {
        viewModelScope.launch {
            repository.updatePurchase(purchase)
        }
    }

    /**
     * Elimina una compra y sus detalles automáticamente.
     * @param purchase La compra a eliminar.
     */
    fun deletePurchase(purchase: PurchaseEntity) {
        viewModelScope.launch {
            repository.deletePurchase(purchase)
        }
    }

    /**
     * Busca una compra específica por su ID.
     * @param id El ID de la compra a buscar.
     * @return StateFlow que emite la compra o null si no existe.
     */
    fun getPurchaseById(id: Int): StateFlow<PurchaseEntity?> {
        return repository.getPurchaseById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }

    /**
     * Obtiene todos los productos de una compra específica.
     * @param purchaseId El ID de la compra.
     * @return StateFlow con la lista de productos de esa compra.
     */
    fun getPurchaseDetails(purchaseId: Int): StateFlow<List<PurchaseDetailEntity>> {
        return repository.getPurchaseDetails(purchaseId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * Elimina todos los detalles de una compra.
     * @param purchaseId El ID de la compra.
     */
    fun deletePurchaseDetails(purchaseId: Int) {
        viewModelScope.launch {
            repository.deletePurchaseDetails(purchaseId)
        }
    }
}
