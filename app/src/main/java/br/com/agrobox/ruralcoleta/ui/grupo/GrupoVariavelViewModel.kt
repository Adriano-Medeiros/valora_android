package br.com.agrobox.ruralcoleta.ui.grupo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity
import br.com.agrobox.ruralcoleta.data.repository.GrupoVariavelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GrupoVariavelViewModel(
    private val repository: GrupoVariavelRepository,
    private val grupoId: Long? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(GrupoVariavelUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarGrupos()

        if (grupoId != null) {
            carregarGrupoParaEdicao(grupoId)
        }
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

    private fun carregarGrupoParaEdicao(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                carregando = true
            )

            val grupo = repository.buscarPorId(id)

            if (grupo == null) {
                _uiState.value = _uiState.value.copy(
                    carregando = false,
                    mensagemErro = "Grupo não encontrado."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                grupoId = grupo.id,
                nome = grupo.nome,
                descricao = grupo.descricao.orEmpty(),
                ordem = grupo.ordem.toString(),
                ativo = grupo.ativo,
                carregando = false,
                mensagemErro = null
            )
        }
    }

    fun alterarNome(nome: String) {
        _uiState.value = _uiState.value.copy(
            nome = nome,
            mensagemErro = null
        )
    }

    fun alterarDescricao(descricao: String) {
        _uiState.value = _uiState.value.copy(
            descricao = descricao
        )
    }

    fun alterarOrdem(ordem: String) {
        _uiState.value = _uiState.value.copy(
            ordem = ordem.filter { it.isDigit() }
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
            _uiState.value = state.copy(
                mensagemErro = "Informe o nome do grupo."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                salvando = true,
                mensagemErro = null
            )

            val grupo = GrupoVariavelEntity(
                id = state.grupoId ?: 0L,
                nome = state.nome.trim(),
                descricao = state.descricao.trim().ifBlank { null },
                ordem = state.ordem.toIntOrNull() ?: 0,
                ativo = state.ativo
            )

            if (state.editando) {
                repository.atualizar(grupo)
            } else {
                repository.salvar(grupo)
            }

            _uiState.value = state.copy(
                salvando = false
            )

            onSuccess()
        }
    }
}
