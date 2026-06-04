package br.com.agrobox.ruralcoleta.ui.coleta.formulario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.OpcaoVariavelRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository

class FormularioDinamicoViewModelFactory(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository,
    private val modeloRepository: ModeloColetaRepository,
    private val respostaRepository: RespostaColetaRepository,
    private val opcaoVariavelRepository: OpcaoVariavelRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(FormularioDinamicoViewModel::class.java)) {
            return FormularioDinamicoViewModel(
                coletaId = coletaId,
                coletaRepository = coletaRepository,
                modeloRepository = modeloRepository,
                respostaRepository = respostaRepository,
                opcaoVariavelRepository = opcaoVariavelRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}