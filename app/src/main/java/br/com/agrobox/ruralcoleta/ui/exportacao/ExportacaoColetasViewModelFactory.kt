package br.com.agrobox.ruralcoleta.ui.exportacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository

class ExportacaoColetasViewModelFactory(
    private val modeloId: Long,
    private val coletaRepository: ColetaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ExportacaoColetasViewModel::class.java)) {
            return ExportacaoColetasViewModel(
                modeloId = modeloId,
                coletaRepository = coletaRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}
