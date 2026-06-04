package br.com.agrobox.ruralcoleta.ui.coleta.resumo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository

class ResumoColetaViewModelFactory(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository,
    private val respostaRepository: RespostaColetaRepository,
    private val benfeitoriaRepository: BenfeitoriaRepository,
    private val fotoColetaRepository: FotoColetaRepository,
    private val modeloColetaRepository: ModeloColetaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ResumoColetaViewModel::class.java)) {
            return ResumoColetaViewModel(
                coletaId = coletaId,
                coletaRepository = coletaRepository,
                respostaRepository = respostaRepository,
                benfeitoriaRepository = benfeitoriaRepository,
                fotoColetaRepository = fotoColetaRepository,
                modeloColetaRepository = modeloColetaRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}