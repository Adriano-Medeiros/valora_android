package br.com.agrobox.ruralcoleta.ui.coleta.fotos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository

class FotosColetaViewModelFactory(
    private val repository: FotoColetaRepository,
    private val coletaId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(FotosColetaViewModel::class.java)) {
            return FotosColetaViewModel(
                repository = repository,
                coletaId = coletaId
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}