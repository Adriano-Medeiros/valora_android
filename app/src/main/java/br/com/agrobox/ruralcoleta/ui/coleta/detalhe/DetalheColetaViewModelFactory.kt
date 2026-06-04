package br.com.agrobox.ruralcoleta.ui.coleta.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoBenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository

class DetalheColetaViewModelFactory(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository,
    private val respostaRepository: RespostaColetaRepository,
    private val benfeitoriaRepository: BenfeitoriaRepository,
    private val fotoColetaRepository: FotoColetaRepository,
    private val fotoBenfeitoriaRepository: FotoBenfeitoriaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(DetalheColetaViewModel::class.java)) {
            return DetalheColetaViewModel(
                coletaId = coletaId,
                coletaRepository = coletaRepository,
                respostaRepository = respostaRepository,
                benfeitoriaRepository = benfeitoriaRepository,
                fotoColetaRepository = fotoColetaRepository,
                fotoBenfeitoriaRepository = fotoBenfeitoriaRepository

            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}