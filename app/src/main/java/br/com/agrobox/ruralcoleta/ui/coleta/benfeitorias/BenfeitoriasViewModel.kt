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
    private val coletaId: Long,
    private val benfeitoriaId: Long? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(BenfeitoriasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarBenfeitorias()

        if (benfeitoriaId != null) {
            carregarBenfeitoriaParaEdicao(benfeitoriaId)
        }
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

    private fun carregarBenfeitoriaParaEdicao(
        benfeitoriaId: Long
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                carregando = true,
                mensagemErro = null
            )

            val benfeitoria = repository.buscarPorId(benfeitoriaId)

            if (benfeitoria == null) {
                _uiState.value = _uiState.value.copy(
                    carregando = false,
                    mensagemErro = "Benfeitoria não encontrada."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                benfeitoriaId = benfeitoria.id,
                categoriaSelecionada = categoriaPorNome(benfeitoria.categoria),
                nome = benfeitoria.nome,
                descricao = benfeitoria.descricao.orEmpty(),
                quantidade = benfeitoria.quantidade?.toString().orEmpty(),
                unidade = benfeitoria.unidade.orEmpty(),
                estadoConservacao = benfeitoria.estadoConservacao.orEmpty(),
                idadeAproximada = benfeitoria.idadeAproximada?.toString().orEmpty(),
                observacao = benfeitoria.observacao.orEmpty(),
                carregando = false,
                mensagemErro = null
            )
        }
    }

    fun alterarCategoria(categoria: CategoriaBenfeitoria) {
        _uiState.value = _uiState.value.copy(
            categoriaSelecionada = categoria,
            mensagemErro = null
        )
    }

    fun alterarNome(nome: String) {
        _uiState.value = _uiState.value.copy(
            nome = nome,
            mensagemErro = null
        )
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
            _uiState.value = state.copy(
                mensagemErro = "Informe o nome da benfeitoria."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                salvando = true,
                mensagemErro = null
            )

            val benfeitoria = BenfeitoriaEntity(
                id = state.benfeitoriaId ?: 0L,
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

            if (state.editando) {
                repository.atualizar(benfeitoria)
            } else {
                repository.salvar(benfeitoria)
            }

            _uiState.value = state.copy(
                salvando = false,
                mensagemErro = null
            )

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

    private fun categoriaPorNome(
        nome: String
    ): CategoriaBenfeitoria {
        return CategoriaBenfeitoria.values()
            .firstOrNull { categoria -> categoria.name == nome }
            ?: CategoriaBenfeitoria.REPRODUTIVA
    }
}
