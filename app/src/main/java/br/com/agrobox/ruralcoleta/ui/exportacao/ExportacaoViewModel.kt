package br.com.agrobox.ruralcoleta.ui.exportacao

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.domain.exportacao.ExportacaoResultado
import br.com.agrobox.ruralcoleta.domain.exportacao.ExportacaoUseCase
import br.com.agrobox.ruralcoleta.domain.exportacao.TipoExportacao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExportacaoViewModel(
    private val exportacaoUseCase: ExportacaoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportacaoUiState())
    val uiState = _uiState.asStateFlow()

    fun exportar(
        context: Context,
        tipo: TipoExportacao,
        coletasIds: List<Long>,
        onSuccess: (ExportacaoResultado) -> Unit
    ) {
        if (coletasIds.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                mensagemErro = "Selecione pelo menos uma coleta para exportar."
            )
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    exportando = true,
                    tipoExportando = tipo,
                    mensagemErro = null
                )

                val resultado = exportacaoUseCase.exportar(
                    context = context,
                    tipo = tipo,
                    coletasIds = coletasIds
                )

                _uiState.value = _uiState.value.copy(
                    exportando = false,
                    tipoExportando = null,
                    mensagemErro = null
                )

                onSuccess(resultado)
            } catch (erro: Exception) {
                _uiState.value = _uiState.value.copy(
                    exportando = false,
                    tipoExportando = null,
                    mensagemErro = erro.message ?: "Erro ao exportar."
                )
            }
        }
    }

    fun limparErro() {
        _uiState.value = _uiState.value.copy(
            mensagemErro = null
        )
    }
}
