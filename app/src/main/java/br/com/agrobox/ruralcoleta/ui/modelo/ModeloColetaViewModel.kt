package br.com.agrobox.ruralcoleta.ui.modelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.VariavelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ModeloColetaViewModel(
    private val modeloRepository: ModeloColetaRepository,
    private val variavelRepository: VariavelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeloColetaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarModelos()
        carregarVariaveis()
    }

    private fun carregarModelos() {
        viewModelScope.launch {
            modeloRepository.listarAtivos().collect { modelos ->
                _uiState.value = _uiState.value.copy(
                    modelos = modelos
                )
            }
        }
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

    fun alterarNome(nome: String) {
        _uiState.value = _uiState.value.copy(nome = nome)
    }

    fun alterarDescricao(descricao: String) {
        _uiState.value = _uiState.value.copy(descricao = descricao)
    }

    fun alterarAtivo(ativo: Boolean) {
        _uiState.value = _uiState.value.copy(ativo = ativo)
    }

    fun alternarVariavelSelecionada(variavelId: Long) {
        val selecionadas = _uiState.value.variaveisSelecionadas.toMutableSet()

        if (selecionadas.contains(variavelId)) {
            selecionadas.remove(variavelId)
        } else {
            selecionadas.add(variavelId)
        }

        _uiState.value = _uiState.value.copy(
            variaveisSelecionadas = selecionadas
        )
    }

    fun salvarModelo(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        if (state.nome.isBlank()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(salvando = true)

            val modelo = ModeloColetaEntity(
                nome = state.nome.trim(),
                descricao = state.descricao.trim().ifBlank { null },
                ativo = state.ativo
            )

            val modeloId = modeloRepository.salvarModelo(modelo)

            modeloRepository.salvarVariaveisDoModelo(
                modeloColetaId = modeloId,
                variaveisIds = state.variaveisSelecionadas.toList()
            )

            _uiState.value = ModeloColetaUiState()

            onSuccess()
        }
    }
}