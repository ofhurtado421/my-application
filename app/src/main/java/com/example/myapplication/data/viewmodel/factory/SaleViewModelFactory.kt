package com.example.myapplication.data.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.SaleRepository
import com.example.myapplication.data.viewmodel.SaleViewModel

/**
 * Factory para crear instancias de SaleViewModel.
 * Es necesaria porque SaleViewModel recibe un parámetro
 * en su constructor (repository), y Android no sabe cómo
 * crearlo solo sin esta clase.
 *
 * Sin el Factory Android intentaría crear el ViewModel
 * con un constructor vacío y lanzaría un error.
 *
 * @param repository El repositorio que se le pasará al ViewModel.
 */
class SaleViewModelFactory(
    private val repository: SaleRepository
) : ViewModelProvider.Factory {

    /**
     * Crea y retorna una instancia de SaleViewModel.
     * Android llama a esta función internamente cuando
     * necesita crear el ViewModel por primera vez.
     *
     * @param modelClass La clase del ViewModel a crear.
     * @return Una instancia nueva de SaleViewModel.
     * @throws IllegalArgumentException si se pide un ViewModel desconocido.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SaleViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}