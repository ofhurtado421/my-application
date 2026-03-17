package com.example.myapplication.data.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.ProductRepository
import com.example.myapplication.data.viewmodel.ProductViewModel

/**
 * Factory para crear instancias de ProductViewModel.
 * Es necesaria porque ProductViewModel recibe un parámetro
 * en su constructor (repository), y Android no sabe cómo
 * crearlo solo sin esta clase.
 *
 * @param repository El repositorio que se le pasará al ViewModel.
 */
class ProductViewModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {

    /**
     * Crea y retorna una instancia de ProductViewModel.
     * Android llama a esta función internamente cuando
     * necesita crear el ViewModel por primera vez.
     *
     * @param modelClass La clase del ViewModel a crear.
     * @return Una instancia nueva de ProductViewModel.
     * @throws IllegalArgumentException si se pide un ViewModel desconocido.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}