package br.com.agrobox.ruralcoleta.ui.coleta.observacoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ObservacoesFinaisViewModel(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ObservacoesFinaisUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarObservacao()
    }

    private fun carregarObservacao() {
        viewModelScope.launch {
            val coleta = coletaRepository.buscarPorId(coletaId)

            _uiState.value = _uiState.value.copy(
                observacao = coleta?.observacao.orEmpty()
            )
        }
    }

    fun alterarObservacao(observacao: String) {
        _uiState.value = _uiState.value.copy(
            observacao = observacao
        )
    }

    fun salvar(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.value = state.copy(salvando = true)

            coletaRepository.atualizarObservacao(
                coletaId = coletaId,
                observacao = state.observacao.trim().ifBlank { null }
            )

            _uiState.value = _uiState.value.copy(
                salvando = false
            )

            onSuccess()
        }
    }
}