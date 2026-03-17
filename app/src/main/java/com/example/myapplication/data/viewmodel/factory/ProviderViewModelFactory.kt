package com.example.myapplication.data.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.ProviderRepository
import com.example.myapplication.data.viewmodel.ProviderViewModel

/**
 * Factory para crear instancias de ProviderViewModel.
 * Es necesaria porque ProviderViewModel recibe un parámetro
 * en su constructor (repository), y Android no sabe cómo
 * crearlo solo sin esta clase.
 *
 * @param repository El repositorio que se le pasará al ViewModel.
 */
class ProviderViewModelFactory(
    private val repository: ProviderRepository
) : ViewModelProvider.Factory {

    /**
     * Crea y retorna una instancia de ProviderViewModel.
     * Android llama a esta función internamente cuando
     * necesita crear el ViewModel por primera vez.
     *
     * @param modelClass La clase del ViewModel a crear.
     * @return Una instancia nueva de ProviderViewModel.
     * @throws IllegalArgumentException si se pide un ViewModel desconocido.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProviderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProviderViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
