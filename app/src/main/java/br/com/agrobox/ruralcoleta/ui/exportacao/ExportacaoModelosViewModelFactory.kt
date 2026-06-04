package br.com.agrobox.ruralcoleta.ui.exportacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository

class ExportacaoModelosViewModelFactory(
    private val modeloColetaRepository: ModeloColetaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ExportacaoModelosViewModel::class.java)) {
            return ExportacaoModelosViewModel(
                modeloColetaRepository = modeloColetaRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}