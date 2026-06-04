package br.com.agrobox.ruralcoleta.ui.coleta.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoBenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalheColetaViewModel(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository,
    private val respostaRepository: RespostaColetaRepository,
    private val benfeitoriaRepository: BenfeitoriaRepository,
    private val fotoColetaRepository: FotoColetaRepository,
    private val fotoBenfeitoriaRepository: FotoBenfeitoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalheColetaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarDetalhes()
    }

    private fun carregarDetalhes() {
        viewModelScope.launch {
            val coleta = coletaRepository.buscarPorId(coletaId)

            _uiState.value = _uiState.value.copy(
                coleta = coleta
            )
        }

        viewModelScope.launch {
            respostaRepository.listarDetalhadasPorColeta(coletaId).collect { respostas ->
                _uiState.value = _uiState.value.copy(
                    respostas = respostas
                )
            }
        }

        viewModelScope.launch {
            benfeitoriaRepository.listarComTotalFotosPorColeta(coletaId).collect { benfeitorias ->
                _uiState.value = _uiState.value.copy(
                    benfeitorias = benfeitorias
                )

                benfeitorias.forEach { benfeitoria ->
                    carregarFotosDaBenfeitoria(
                        benfeitoriaId = benfeitoria.id
                    )
                }
            }
        }

        viewModelScope.launch {
            fotoColetaRepository.listarPorColeta(coletaId).collect { fotos ->
                _uiState.value = _uiState.value.copy(
                    fotos = fotos
                )
            }
        }
    }

    private fun carregarFotosDaBenfeitoria(
        benfeitoriaId: Long
    ) {
        viewModelScope.launch {
            fotoBenfeitoriaRepository.listarPorBenfeitoria(benfeitoriaId).collect { fotos ->
                val mapaAtual = _uiState.value.fotosPorBenfeitoria.toMutableMap()

                mapaAtual[benfeitoriaId] = fotos

                _uiState.value = _uiState.value.copy(
                    fotosPorBenfeitoria = mapaAtual
                )
            }
        }
    }

    fun excluirColeta(
        onSuccess: () -> Unit
    ) {
        val coleta: ColetaEntity = _uiState.value.coleta ?: return

        viewModelScope.launch {
            coletaRepository.excluir(coleta)

            onSuccess()
        }
    }
}