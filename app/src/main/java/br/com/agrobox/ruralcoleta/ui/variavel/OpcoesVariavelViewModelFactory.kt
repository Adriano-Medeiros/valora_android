package br.com.agrobox.ruralcoleta.ui.variavel.opcoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.OpcaoVariavelRepository

class OpcoesVariavelViewModelFactory(
    private val repository: OpcaoVariavelRepository,
    private val variavelId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OpcoesVariavelViewModel::class.java)) {
            return OpcoesVariavelViewModel(repository, variavelId) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}