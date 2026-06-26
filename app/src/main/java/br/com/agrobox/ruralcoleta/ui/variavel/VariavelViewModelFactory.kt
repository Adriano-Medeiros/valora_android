package br.com.agrobox.ruralcoleta.ui.variavel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.GrupoVariavelRepository
import br.com.agrobox.ruralcoleta.data.repository.VariavelRepository

class VariavelViewModelFactory(
    private val variavelRepository: VariavelRepository,
    private val grupoRepository: GrupoVariavelRepository,
    private val variavelId: Long? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(VariavelViewModel::class.java)) {
            return VariavelViewModel(
                variavelRepository = variavelRepository,
                grupoRepository = grupoRepository,
                variavelId = variavelId
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}
