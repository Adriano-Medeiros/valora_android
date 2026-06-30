package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.fotos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.repository.FotoBenfeitoriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class FotosBenfeitoriaViewModel(
    private val repository: FotoBenfeitoriaRepository,
    private val benfeitoriaId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(FotosBenfeitoriaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarFotos()
    }

    private fun carregarFotos() {
        viewModelScope.launch {
            repository.listarPorBenfeitoria(benfeitoriaId).collect { fotos ->
                _uiState.value = _uiState.value.copy(
                    fotos = fotos
                )
            }
        }
    }

    fun alterarLegenda(
        legenda: String
    ) {
        _uiState.value = _uiState.value.copy(
            legenda = legenda
        )
    }

    fun alterarObservacao(
        observacao: String
    ) {
        _uiState.value = _uiState.value.copy(
            observacao = observacao
        )
    }

    fun salvarFoto(
        caminho: String
    ) {
        viewModelScope.launch {
            val state = _uiState.value

            repository.salvar(
                FotoBenfeitoriaEntity(
                    benfeitoriaId = benfeitoriaId,
                    caminhoArquivo = caminho,
                    legenda = state.legenda.ifBlank { null },
                    observacao = state.observacao.ifBlank { null }
                )
            )

            _uiState.value = _uiState.value.copy(
                legenda = "",
                observacao = ""
            )
        }
    }

    fun atualizarFoto(
        foto: FotoBenfeitoriaEntity,
        legenda: String,
        observacao: String
    ) {
        viewModelScope.launch {
            repository.atualizar(
                foto.copy(
                    legenda = legenda.ifBlank { null },
                    observacao = observacao.ifBlank { null }
                )
            )
        }
    }

    fun excluirFoto(
        foto: FotoBenfeitoriaEntity
    ) {
        viewModelScope.launch {
            repository.excluir(foto)

            val arquivo = File(foto.caminhoArquivo)

            if (arquivo.exists()) {
                arquivo.delete()
            }
        }
    }
}
