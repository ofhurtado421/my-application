package com.example.myapplication.data.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.ContactRepository
import com.example.myapplication.data.viewmodel.ContactViewModel

/**
 * Factory para crear instancias de ContactViewModel.
 * Es necesaria porque ContactViewModel recibe un parámetro
 * en su constructor (repository), y Android no sabe cómo
 * crearlo solo sin esta clase.
 *
 * @param repository El repositorio que se le pasará al ViewModel.
 */
class ContactViewModelFactory(
    private val repository: ContactRepository
) : ViewModelProvider.Factory {

    /**
     * Crea y retorna una instancia de ContactViewModel.
     * Android llama a esta función internamente cuando
     * necesita crear el ViewModel por primera vez.
     *
     * @param modelClass La clase del ViewModel a crear.
     * @return Una instancia nueva de ContactViewModel.
     * @throws IllegalArgumentException si se pide un ViewModel desconocido.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que se está pidiendo exactamente ContactViewModel
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Cast seguro porque ya verificamos el tipo
            return ContactViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}