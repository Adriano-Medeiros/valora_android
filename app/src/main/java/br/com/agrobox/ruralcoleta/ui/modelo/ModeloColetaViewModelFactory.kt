package br.com.agrobox.ruralcoleta.ui.modelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.VariavelRepository

class ModeloColetaViewModelFactory(
    private val modeloRepository: ModeloColetaRepository,
    private val variavelRepository: VariavelRepository,
    private val modeloId: Long? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ModeloColetaViewModel::class.java)) {
            return ModeloColetaViewModel(
                modeloRepository = modeloRepository,
                variavelRepository = variavelRepository,
                modeloId = modeloId
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}
