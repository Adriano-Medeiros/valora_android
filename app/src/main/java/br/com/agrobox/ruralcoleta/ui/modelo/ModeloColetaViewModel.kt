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
    private val variavelRepository: VariavelRepository,
    private val modeloId: Long? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeloColetaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarModelos()
        carregarVariaveis()

        if (modeloId != null) {
            carregarModeloParaEdicao(modeloId)
        }
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

    private fun carregarModeloParaEdicao(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                carregando = true
            )

            val modelo = modeloRepository.buscarModeloPorId(id)

            if (modelo == null) {
                _uiState.value = _uiState.value.copy(
                    carregando = false,
                    mensagemErro = "Formulário não encontrado."
                )
                return@launch
            }

            val idsSelecionadosOrdenados = modeloRepository
                .listarIdsVariaveisDoModelo(id)

            _uiState.value = _uiState.value.copy(
                modeloId = modelo.id,
                nome = modelo.nome,
                descricao = modelo.descricao.orEmpty(),
                ativo = modelo.ativo,
                variaveisSelecionadas = idsSelecionadosOrdenados,
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

    fun alterarAtivo(ativo: Boolean) {
        _uiState.value = _uiState.value.copy(
            ativo = ativo
        )
    }

    fun alternarVariavelSelecionada(variavelId: Long) {
        val selecionadas = _uiState.value.variaveisSelecionadas.toMutableList()

        if (selecionadas.contains(variavelId)) {
            selecionadas.remove(variavelId)
        } else {
            selecionadas.add(variavelId)
        }

        _uiState.value = _uiState.value.copy(
            variaveisSelecionadas = selecionadas
        )
    }

    fun moverVariavelParaCima(variavelId: Long) {
        val selecionadas = _uiState.value.variaveisSelecionadas.toMutableList()
        val index = selecionadas.indexOf(variavelId)

        if (index > 0) {
            val anterior = selecionadas[index - 1]
            selecionadas[index - 1] = variavelId
            selecionadas[index] = anterior

            _uiState.value = _uiState.value.copy(
                variaveisSelecionadas = selecionadas
            )
        }
    }

    fun moverVariavelParaBaixo(variavelId: Long) {
        val selecionadas = _uiState.value.variaveisSelecionadas.toMutableList()
        val index = selecionadas.indexOf(variavelId)

        if (index >= 0 && index < selecionadas.lastIndex) {
            val proxima = selecionadas[index + 1]
            selecionadas[index + 1] = variavelId
            selecionadas[index] = proxima

            _uiState.value = _uiState.value.copy(
                variaveisSelecionadas = selecionadas
            )
        }
    }

    fun salvarModelo(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        if (state.nome.isBlank()) {
            _uiState.value = state.copy(
                mensagemErro = "Informe o nome do formulário."
            )
            return
        }

        if (state.variaveisSelecionadas.isEmpty()) {
            _uiState.value = state.copy(
                mensagemErro = "Selecione pelo menos uma variável para o formulário."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                salvando = true,
                mensagemErro = null
            )

            val modelo = ModeloColetaEntity(
                id = state.modeloId ?: 0L,
                nome = state.nome.trim(),
                descricao = state.descricao.trim().ifBlank { null },
                ativo = state.ativo
            )

            val modeloIdSalvo = if (state.editando) {
                modeloRepository.atualizarModelo(modelo)
                modelo.id
            } else {
                modeloRepository.salvarModelo(modelo)
            }

            modeloRepository.salvarVariaveisDoModelo(
                modeloColetaId = modeloIdSalvo,
                variaveisIds = state.variaveisSelecionadas
            )

            _uiState.value = _uiState.value.copy(
                salvando = false
            )

            onSuccess()
        }
    }
}