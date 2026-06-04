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

    fun salvarFoto(
        caminho: String
    ) {
        viewModelScope.launch {
            val legendaAtual = _uiState.value.legenda

            repository.salvar(
                FotoColetaEntity(
                    coletaId = coletaId,
                    caminhoArquivo = caminho,
                    legenda = legendaAtual.ifBlank { null }
                )
            )

            _uiState.value = _uiState.value.copy(
                legenda = ""
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