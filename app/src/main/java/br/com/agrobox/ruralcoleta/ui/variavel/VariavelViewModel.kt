package br.com.agrobox.ruralcoleta.ui.variavel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.TipoCampo
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import br.com.agrobox.ruralcoleta.data.repository.GrupoVariavelRepository
import br.com.agrobox.ruralcoleta.data.repository.VariavelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VariavelViewModel(
    private val variavelRepository: VariavelRepository,
    private val grupoRepository: GrupoVariavelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VariavelUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarVariaveis()
        carregarGrupos()
    }

    private fun carregarVariaveis() {
        viewModelScope.launch {
            variavelRepository.listarAtivas().collect { variaveis ->
                _uiState.value = _uiState.value.copy(
                    variaveis = variaveis
                )
            }
        }
    }

    private fun carregarGrupos() {
        viewModelScope.launch {
            grupoRepository.listarAtivos().collect { grupos ->
                _uiState.value = _uiState.value.copy(
                    grupos = grupos
                )
            }
        }
    }

    fun alterarNome(nome: String) {
        _uiState.value = _uiState.value.copy(nome = nome)
    }

    fun alterarTipoCampo(tipoCampo: TipoCampo) {
        _uiState.value = _uiState.value.copy(tipoCampo = tipoCampo)
    }

    fun alterarUnidade(unidade: String) {
        _uiState.value = _uiState.value.copy(unidade = unidade)
    }

    fun alterarGrupo(grupoId: Long?) {
        _uiState.value = _uiState.value.copy(grupoId = grupoId)
    }

    fun alterarObrigatoria(obrigatoria: Boolean) {
        _uiState.value = _uiState.value.copy(obrigatoria = obrigatoria)
    }

    fun alterarDica(dica: String) {
        _uiState.value = _uiState.value.copy(dica = dica)
    }

    fun alterarAtivo(ativo: Boolean) {
        _uiState.value = _uiState.value.copy(ativo = ativo)
    }

    fun salvarVariavel(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        if (state.nome.isBlank()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(salvando = true)

            val variavel = VariavelEntity(
                nome = state.nome.trim(),
                tipoCampo = state.tipoCampo.name,
                unidade = state.unidade.trim().ifBlank { null },
                grupoId = state.grupoId,
                obrigatoria = state.obrigatoria,
                dica = state.dica.trim().ifBlank { null },
                ativo = state.ativo
            )

            variavelRepository.salvar(variavel)

            _uiState.value = VariavelUiState()

            onSuccess()
        }
    }
}