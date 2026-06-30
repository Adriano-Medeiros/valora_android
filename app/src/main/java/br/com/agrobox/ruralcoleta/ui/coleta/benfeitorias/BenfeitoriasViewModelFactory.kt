package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository

class BenfeitoriasViewModelFactory(
    private val repository: BenfeitoriaRepository,
    private val coletaId: Long,
    private val benfeitoriaId: Long? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(BenfeitoriasViewModel::class.java)) {
            return BenfeitoriasViewModel(
                repository = repository,
                coletaId = coletaId,
                benfeitoriaId = benfeitoriaId
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}
