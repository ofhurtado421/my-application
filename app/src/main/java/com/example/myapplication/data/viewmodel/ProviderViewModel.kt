package com.example.myapplication.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.ProviderEntity
import com.example.myapplication.data.repository.ProviderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de proveedores.
 * Actúa como intermediario entre el Repository y la UI (pantallas).
 * Sobrevive a los cambios de configuración como rotar la pantalla,
 * evitando que los datos se pierdan o se recarguen innecesariamente.
 *
 * @param repository El repositorio inyectado para acceder a los datos.
 */
class ProviderViewModel(private val repository: ProviderRepository) : ViewModel() {

    /**
     * Lista de todos los proveedores expuesta a la UI como StateFlow.
     * Ordenada alfabéticamente por nombre comercial (A-Z).
     * La UI observa este Flow y se actualiza automáticamente cuando cambia.
     */
    val allProviders: StateFlow<List<ProviderEntity>> = repository.allProviders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Inserta un nuevo proveedor en la base de datos.
     * Se ejecuta en viewModelScope para no bloquear el hilo principal.
     * @param provider El proveedor a insertar.
     */
    fun insert(provider: ProviderEntity) {
        viewModelScope.launch {
            repository.insert(provider)
        }
    }

    /**
     * Actualiza un proveedor existente en la base de datos.
     * @param provider El proveedor con los datos actualizados.
     */
    fun update(provider: ProviderEntity) {
        viewModelScope.launch {
            repository.update(provider)
        }
    }

    /**
     * Elimina un proveedor de la base de datos.
     * @param provider El proveedor a eliminar.
     */
    fun delete(provider: ProviderEntity) {
        viewModelScope.launch {
            repository.delete(provider)
        }
    }

    /**
     * Busca un proveedor específico por su ID.
     * @param id El ID del proveedor a buscar.
     * @return StateFlow que emite el proveedor encontrado o null si no existe.
     */
    fun getProviderById(id: Int): StateFlow<ProviderEntity?> {
        return repository.getProviderById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }
}
