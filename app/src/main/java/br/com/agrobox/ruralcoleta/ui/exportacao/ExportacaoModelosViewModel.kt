package br.com.agrobox.ruralcoleta.ui.exportacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExportacaoModelosViewModel(
    private val modeloColetaRepository: ModeloColetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportacaoModelosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarModelos()
    }

    private fun carregarModelos() {
        viewModelScope.launch {
            modeloColetaRepository.listarAtivos().collect { modelos ->
                _uiState.value = _uiState.value.copy(
                    modelos = modelos
                )
            }
        }
    }
}