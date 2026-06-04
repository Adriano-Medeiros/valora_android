package br.com.agrobox.ruralcoleta.ui.variavel.opcoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.OpcaoVariavelEntity
import br.com.agrobox.ruralcoleta.data.repository.OpcaoVariavelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OpcoesVariavelViewModel(
    private val repository: OpcaoVariavelRepository,
    private val variavelId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(OpcoesVariavelUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarOpcoes()
    }

    private fun carregarOpcoes() {
        viewModelScope.launch {
            repository.listarPorVariavel(variavelId).collect { opcoes ->
                _uiState.value = _uiState.value.copy(opcoes = opcoes)
            }
        }
    }

    fun alterarDescricao(descricao: String) {
        _uiState.value = _uiState.value.copy(descricao = descricao)
    }

    fun alterarOrdem(ordem: String) {
        _uiState.value = _uiState.value.copy(ordem = ordem)
    }

    fun salvarOpcao() {
        val state = _uiState.value

        if (state.descricao.isBlank()) return

        viewModelScope.launch {
            repository.salvar(
                OpcaoVariavelEntity(
                    variavelId = variavelId,
                    descricao = state.descricao.trim(),
                    ordem = state.ordem.toIntOrNull() ?: 0,
                    ativo = true
                )
            )

            _uiState.value = state.copy(
                descricao = "",
                ordem = "0"
            )
        }
    }

    fun excluirOpcao(opcao: OpcaoVariavelEntity) {
        viewModelScope.launch {
            repository.excluir(opcao)
        }
    }
}