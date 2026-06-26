package br.com.agrobox.ruralcoleta.ui.grupo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.GrupoVariavelRepository

class GrupoVariavelViewModelFactory(
    private val repository: GrupoVariavelRepository,
    private val grupoId: Long? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(GrupoVariavelViewModel::class.java)) {
            return GrupoVariavelViewModel(
                repository = repository,
                grupoId = grupoId
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}
