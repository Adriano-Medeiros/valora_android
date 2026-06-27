package br.com.agrobox.ruralcoleta.ui.exportacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExportacaoColetasViewModel(
    private val modeloId: Long,
    private val coletaRepository: ColetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportacaoColetasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarColetas()
    }

    private fun carregarColetas() {
        viewModelScope.launch {
            coletaRepository.listarPorModelo(modeloId).collect { coletas ->
                val selecionadasValidas = _uiState.value.selecionadas
                    .filter { idSelecionado ->
                        coletas.any { coleta -> coleta.id == idSelecionado }
                    }
                    .toSet()

                _uiState.value = _uiState.value.copy(
                    coletas = coletas,
                    selecionadas = selecionadasValidas
                )
            }
        }
    }

    fun alternarSelecao(coletaId: Long) {
        val selecionadas = _uiState.value.selecionadas.toMutableSet()

        if (selecionadas.contains(coletaId)) {
            selecionadas.remove(coletaId)
        } else {
            selecionadas.add(coletaId)
        }

        _uiState.value = _uiState.value.copy(
            selecionadas = selecionadas,
            mensagemErro = null
        )
    }

    fun selecionarTodas() {
        _uiState.value = _uiState.value.copy(
            selecionadas = _uiState.value.coletas.map { coleta -> coleta.id }.toSet(),
            mensagemErro = null
        )
    }

    fun limparSelecao() {
        _uiState.value = _uiState.value.copy(
            selecionadas = emptySet(),
            mensagemErro = null
        )
    }

    fun validarSelecao(): Boolean {
        val existeSelecao = _uiState.value.selecionadas.isNotEmpty()

        if (!existeSelecao) {
            _uiState.value = _uiState.value.copy(
                mensagemErro = "Selecione pelo menos uma coleta para exportar."
            )
        }

        return existeSelecao
    }

    fun limparErro() {
        _uiState.value = _uiState.value.copy(
            mensagemErro = null
        )
    }
}
