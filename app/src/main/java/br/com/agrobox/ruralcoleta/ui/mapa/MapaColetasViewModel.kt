package br.com.agrobox.ruralcoleta.ui.mapa

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.StatusColeta
import br.com.agrobox.ruralcoleta.data.local.entity.TipoColeta
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaColetasViewModel(
    private val coletaRepository: ColetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapaColetasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarColetas()
    }

    private fun carregarColetas() {
        viewModelScope.launch {
            coletaRepository.listarTodas().collect { coletas ->

                coletas.forEach {
                    Log.d(
                        "MAPA",
                        "ID=${it.id} | ${it.nomeReferencia} | TIPO=${it.tipoColeta} | STATUS=${it.status} | LAT=${it.latitude} | LON=${it.longitude}"
                    )
                }

                val coletasComCoordenadas = coletas.filter { coleta ->
                    coleta.latitude != null &&
                            coleta.longitude != null &&
                            coleta.latitude != 0.0 &&
                            coleta.longitude != 0.0
                }

                val novoState = _uiState.value.copy(
                    coletas = coletasComCoordenadas
                )

                _uiState.value = novoState.copy(
                    coletasFiltradas = filtrarColetas(novoState)
                )
            }
        }
    }

    fun alternarAvaliando() {
        atualizarFiltros {
            it.copy(mostrarAvaliando = !it.mostrarAvaliando)
        }
    }

    fun alternarAmostral() {
        atualizarFiltros {
            it.copy(mostrarAmostral = !it.mostrarAmostral)
        }
    }

    fun alternarRascunho() {
        atualizarFiltros {
            it.copy(mostrarRascunho = !it.mostrarRascunho)
        }
    }

    fun alternarConcluida() {
        atualizarFiltros {
            it.copy(mostrarConcluida = !it.mostrarConcluida)
        }
    }

    private fun atualizarFiltros(
        transform: (MapaColetasUiState) -> MapaColetasUiState
    ) {
        val novoState = transform(_uiState.value)

        _uiState.value = novoState.copy(
            coletasFiltradas = filtrarColetas(novoState)
        )
    }

    private fun filtrarColetas(
        state: MapaColetasUiState
    ): List<ColetaEntity> {
        return state.coletas.filter { coleta ->

            val tipo = coleta.tipoColeta.uppercase()
            val status = coleta.status.uppercase()

            val tipoOk = when {
                tipo.contains(TipoColeta.AVALIANDO.name) -> state.mostrarAvaliando
                tipo.contains(TipoColeta.AMOSTRAL.name) -> state.mostrarAmostral
                tipo.contains("AMOSTRA") -> state.mostrarAmostral
                else -> true
            }

            val statusOk = when {
                status.contains(StatusColeta.RASCUNHO.name) -> state.mostrarRascunho
                status.contains(StatusColeta.CONCLUIDA.name) -> state.mostrarConcluida
                status.contains("CONCLUÍDA") -> state.mostrarConcluida
                else -> true
            }

            tipoOk && statusOk
        }
    }
}