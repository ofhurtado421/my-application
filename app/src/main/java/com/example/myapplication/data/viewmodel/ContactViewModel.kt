package com.example.myapplication.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.ContactEntity
import com.example.myapplication.data.repository.ContactRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel de contactos.
 * Actúa como intermediario entre el Repository y la UI (pantallas).
 * Sobrevive a los cambios de configuración como rotar la pantalla,
 * evitando que los datos se pierdan o se recarguen innecesariamente.
 *
 * @param repository El repositorio inyectado para acceder a los datos.
 */
class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    /**
     * Lista de todos los contactos expuesta a la UI como StateFlow.
     * StateFlow siempre tiene un valor actual (empieza con lista vacía).
     * La UI observa este Flow y se actualiza automáticamente cuando cambia.
     *
     * stateIn convierte el Flow del repositorio en StateFlow:
     *   - scope: viewModelScope → se cancela cuando el ViewModel se destruye
     *   - started: SharingStarted.WhileSubscribed(5000) → se mantiene activo
     *              5 segundos después de que la UI deje de observar,
     *              útil para sobrevivir rotaciones de pantalla
     *   - initialValue: emptyList() → valor inicial mientras carga
     */
    val allContacts: StateFlow<List<ContactEntity>> = repository.allContacts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Inserta un nuevo contacto en la base de datos.
     * Se ejecuta en viewModelScope para no bloquear el hilo principal.
     * @param contact El contacto a insertar.
     */
    fun insert(contact: ContactEntity) {
        viewModelScope.launch {
            repository.insert(contact)
        }
    }

    /**
     * Actualiza un contacto existente en la base de datos.
     * Room identifica el registro por el ID del objeto.
     * @param contact El contacto con los datos actualizados.
     */
    fun update(contact: ContactEntity) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }

    /**
     * Elimina un contacto de la base de datos.
     * Room identifica el registro por el ID del objeto.
     * @param contact El contacto a eliminar.
     */
    fun delete(contact: ContactEntity) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }

    /**
     * Busca un contacto específico por su ID.
     * @param id El ID del contacto a buscar.
     * @return StateFlow que emite el contacto encontrado o null si no existe.
     */
    fun getContactById(id: Int): StateFlow<ContactEntity?> {
        return repository.getContactById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }
}