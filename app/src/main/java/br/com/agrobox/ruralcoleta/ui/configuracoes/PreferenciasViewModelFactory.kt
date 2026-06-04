package br.com.agrobox.ruralcoleta.ui.configuracoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.PreferenciasRepository

class PreferenciasViewModelFactory(
    private val repository: PreferenciasRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(PreferenciasViewModel::class.java)) {
            return PreferenciasViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}