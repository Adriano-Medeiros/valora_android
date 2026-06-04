package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.CategoriaBenfeitoria
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BenfeitoriasViewModel(
    private val repository: BenfeitoriaRepository,
    private val coletaId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(BenfeitoriasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarBenfeitorias()
    }

    private fun carregarBenfeitorias() {
        viewModelScope.launch {
            repository.listarPorColeta(coletaId).collect { lista ->
                _uiState.value = _uiState.value.copy(
                    benfeitorias = lista
                )
            }
        }
    }

    fun alterarCategoria(categoria: CategoriaBenfeitoria) {
        _uiState.value = _uiState.value.copy(categoriaSelecionada = categoria)
    }

    fun alterarNome(nome: String) {
        _uiState.value = _uiState.value.copy(nome = nome)
    }

    fun alterarDescricao(descricao: String) {
        _uiState.value = _uiState.value.copy(descricao = descricao)
    }

    fun alterarQuantidade(quantidade: String) {
        _uiState.value = _uiState.value.copy(quantidade = quantidade)
    }

    fun alterarUnidade(unidade: String) {
        _uiState.value = _uiState.value.copy(unidade = unidade)
    }

    fun alterarEstadoConservacao(estado: String) {
        _uiState.value = _uiState.value.copy(estadoConservacao = estado)
    }

    fun alterarIdadeAproximada(idade: String) {
        _uiState.value = _uiState.value.copy(idadeAproximada = idade)
    }

    fun alterarObservacao(observacao: String) {
        _uiState.value = _uiState.value.copy(observacao = observacao)
    }

    fun salvarBenfeitoria(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        if (state.nome.isBlank()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(salvando = true)

            val benfeitoria = BenfeitoriaEntity(
                coletaId = coletaId,
                categoria = state.categoriaSelecionada.name,
                nome = state.nome.trim(),
                descricao = state.descricao.trim().ifBlank { null },
                quantidade = state.quantidade.replace(",", ".").toDoubleOrNull(),
                unidade = state.unidade.trim().ifBlank { null },
                estadoConservacao = state.estadoConservacao.trim().ifBlank { null },
                idadeAproximada = state.idadeAproximada.toIntOrNull(),
                observacao = state.observacao.trim().ifBlank { null }
            )

            repository.salvar(benfeitoria)

            _uiState.value = BenfeitoriasUiState()

            onSuccess()
        }
    }

    fun excluirBenfeitoria(
        benfeitoria: BenfeitoriaEntity
    ) {
        viewModelScope.launch {
            repository.excluir(benfeitoria)
        }
    }
}