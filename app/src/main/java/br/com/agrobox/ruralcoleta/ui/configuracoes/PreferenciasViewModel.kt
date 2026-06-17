package br.com.agrobox.ruralcoleta.ui.configuracoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.repository.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PreferenciasViewModel(
    private val repository: PreferenciasRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreferenciasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observarPreferencias()
    }

    private fun observarPreferencias() {
        viewModelScope.launch {
            repository.capturarGpsAutomaticamente.collect { valor ->
                _uiState.value = _uiState.value.copy(
                    capturarGpsAutomaticamente = valor
                )
            }
        }

        viewModelScope.launch {
            repository.mostrarRascunhosDashboard.collect { valor ->
                _uiState.value = _uiState.value.copy(
                    mostrarRascunhosDashboard = valor
                )
            }
        }

        viewModelScope.launch {
            repository.periodoAtividadesRecentes.collect { valor ->
                _uiState.value = _uiState.value.copy(
                    periodoAtividadesRecentes = valor
                )
            }
        }

        viewModelScope.launch {
            repository.tutorialPrimeiroAcessoConcluido.collect { valor ->
                _uiState.value = _uiState.value.copy(
                    tutorialPrimeiroAcessoConcluido = valor
                )
            }
        }
    }

    fun alterarCapturarGpsAutomaticamente(
        ativo: Boolean
    ) {
        repository.alterarCapturarGpsAutomaticamente(ativo)
    }

    fun alterarMostrarRascunhosDashboard(
        ativo: Boolean
    ) {
        repository.alterarMostrarRascunhosDashboard(ativo)
    }

    fun alterarPeriodoAtividadesRecentes(
        dias: Int
    ) {
        repository.alterarPeriodoAtividadesRecentes(dias)
    }

    fun reiniciarTutorialPrimeiroAcesso() {
        repository.reiniciarTutorialPrimeiroAcesso()
    }
}