package com.example.myapplication.data.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.PurchaseRepository
import com.example.myapplication.data.viewmodel.PurchaseViewModel

/**
 * Factory para crear instancias de PurchaseViewModel.
 * Es necesaria porque PurchaseViewModel recibe un parámetro
 * en su constructor (repository), y Android no sabe cómo
 * crearlo solo sin esta clase.
 *
 * Sin el Factory Android intentaría crear el ViewModel
 * con un constructor vacío y lanzaría un error.
 *
 * @param repository El repositorio que se le pasará al ViewModel.
 */
class PurchaseViewModelFactory(
    private val repository: PurchaseRepository
) : ViewModelProvider.Factory {

    /**
     * Crea y retorna una instancia de PurchaseViewModel.
     * @param modelClass La clase del ViewModel a crear.
     * @return Una instancia nueva de PurchaseViewModel.
     * @throws IllegalArgumentException si se pide un ViewModel desconocido.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PurchaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PurchaseViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}