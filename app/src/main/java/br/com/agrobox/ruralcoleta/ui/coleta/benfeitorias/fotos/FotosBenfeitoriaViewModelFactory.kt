package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.fotos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.FotoBenfeitoriaRepository

class FotosBenfeitoriaViewModelFactory(
    private val repository: FotoBenfeitoriaRepository,
    private val benfeitoriaId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(FotosBenfeitoriaViewModel::class.java)) {
            return FotosBenfeitoriaViewModel(
                repository = repository,
                benfeitoriaId = benfeitoriaId
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida: ${modelClass.name}")
    }
}