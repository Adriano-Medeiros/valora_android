package br.com.agrobox.ruralcoleta.ui.grupo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.GrupoVariavelRepository

class GrupoVariavelViewModelFactory(
    private val repository: GrupoVariavelRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(GrupoVariavelViewModel::class.java)) {
            return GrupoVariavelViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}