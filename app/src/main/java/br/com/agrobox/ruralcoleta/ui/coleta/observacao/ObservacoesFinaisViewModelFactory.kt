package br.com.agrobox.ruralcoleta.ui.coleta.observacoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository

class ObservacoesFinaisViewModelFactory(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ObservacoesFinaisViewModel::class.java)) {
            return ObservacoesFinaisViewModel(
                coletaId = coletaId,
                coletaRepository = coletaRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}