package br.com.agrobox.ruralcoleta.ui.coleta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository

class NovaColetaViewModelFactory(
    private val repository: ColetaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NovaColetaViewModel::class.java)) {
            return NovaColetaViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}