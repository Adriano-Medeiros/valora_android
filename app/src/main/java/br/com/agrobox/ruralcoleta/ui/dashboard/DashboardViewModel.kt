package br.com.agrobox.ruralcoleta.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.StatusColeta
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val coletaRepository: ColetaRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DashboardUiState()
    )

    val uiState = _uiState.asStateFlow()

    init {
        observarDashboard()
    }

    private fun observarDashboard() {

        viewModelScope.launch {

            combine(
                coletaRepository.listarTodas(),
                preferenciasRepository.mostrarRascunhosDashboard
            ) { coletas, mostrarRascunhos ->

                val coletasDashboard = if (mostrarRascunhos) {
                    coletas
                } else {
                    coletas.filter {
                        it.status != StatusColeta.RASCUNHO.name
                    }
                }

                DashboardUiState(
                    coletas = coletasDashboard,

                    totalColetas = coletas.size,

                    totalRascunhos = coletas.count {
                        it.status == StatusColeta.RASCUNHO.name
                    },

                    totalConcluidas = coletas.count {
                        it.status == StatusColeta.CONCLUIDA.name
                    },

                    totalAvaliando = coletas.count {
                        it.tipoColeta == "AVALIANDO"
                    },

                    totalAmostral = coletas.count {
                        it.tipoColeta == "AMOSTRAL"
                    }
                )
            }.collect { state ->

                _uiState.value = state
            }
        }
    }
}