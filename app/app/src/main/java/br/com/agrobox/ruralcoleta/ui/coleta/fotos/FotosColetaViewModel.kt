package br.com.agrobox.ruralcoleta.ui.coleta.fotos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class FotosColetaViewModel(
    private val repository: FotoColetaRepository,
    private val coletaId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(FotosColetaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarFotos()
    }

    private fun carregarFotos() {
        viewModelScope.launch {
            repository.listarPorColeta(coletaId).collect { fotos ->
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
                FotoColetaEntity(
                    coletaId = coletaId,
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
        foto: FotoColetaEntity,
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
        foto: FotoColetaEntity
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
