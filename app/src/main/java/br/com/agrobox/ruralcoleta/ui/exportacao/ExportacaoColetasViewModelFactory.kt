package br.com.agrobox.ruralcoleta.ui.exportacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository

class ExportacaoColetasViewModelFactory(
    private val modeloId: Long,
    private val coletaRepository: ColetaRepository,
    private val modeloColetaRepository: ModeloColetaRepository,
    private val respostaColetaRepository: RespostaColetaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ExportacaoColetasViewModel::class.java)) {
            return ExportacaoColetasViewModel(
                modeloId = modeloId,
                coletaRepository = coletaRepository,
                modeloColetaRepository = modeloColetaRepository,
                respostaColetaRepository = respostaColetaRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}