package br.com.agrobox.ruralcoleta.ui.grupo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity
import br.com.agrobox.ruralcoleta.data.repository.GrupoVariavelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GrupoVariavelViewModel(
    private val repository: GrupoVariavelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GrupoVariavelUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarGrupos()
    }

    private fun carregarGrupos() {
        viewModelScope.launch {
            repository.listarAtivos().collect { grupos ->
                _uiState.value = _uiState.value.copy(
                    grupos = grupos
                )
            }
        }
    }

    fun alterarNome(nome: String) {
        _uiState.value = _uiState.value.copy(
            nome = nome
        )
    }

    fun alterarDescricao(descricao: String) {
        _uiState.value = _uiState.value.copy(
            descricao = descricao
        )
    }

    fun alterarOrdem(ordem: String) {
        _uiState.value = _uiState.value.copy(
            ordem = ordem
        )
    }

    fun alterarAtivo(ativo: Boolean) {
        _uiState.value = _uiState.value.copy(
            ativo = ativo
        )
    }

    fun salvarGrupo(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        if (state.nome.isBlank()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                salvando = true
            )

            val grupo = GrupoVariavelEntity(
                nome = state.nome.trim(),
                descricao = state.descricao.trim().ifBlank { null },
                ordem = state.ordem.toIntOrNull() ?: 0,
                ativo = state.ativo
            )

            repository.salvar(grupo)

            _uiState.value = GrupoVariavelUiState()

            onSuccess()
        }
    }
}