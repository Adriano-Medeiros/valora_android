package br.com.agrobox.ruralcoleta.ui.backup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.backup.BackupService
import br.com.agrobox.ruralcoleta.util.ShareHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BackupViewModel(
    private val backupService: BackupService = BackupService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BackupUiState())
    val uiState = _uiState.asStateFlow()

    fun gerarBackup(
        context: Context
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    processando = true,
                    mensagemSucesso = null,
                    mensagemErro = null,
                    restauracaoConcluida = false
                )

                val arquivoBackup = backupService.gerarBackupCompleto(context)

                _uiState.value = _uiState.value.copy(
                    processando = false,
                    mensagemSucesso = "Backup gerado com sucesso.",
                    mensagemErro = null
                )

                ShareHelper.compartilharArquivo(
                    context = context,
                    file = arquivoBackup,
                    mimeType = "application/zip",
                    titulo = "Compartilhar backup do RuralColeta"
                )
            } catch (erro: Exception) {
                _uiState.value = _uiState.value.copy(
                    processando = false,
                    mensagemSucesso = null,
                    mensagemErro = erro.message ?: "Erro ao gerar backup."
                )
            }
        }
    }

    fun restaurarBackup(
        context: Context,
        backupUri: Uri
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    processando = true,
                    mensagemSucesso = null,
                    mensagemErro = null,
                    restauracaoConcluida = false
                )

                backupService.restaurarBackupCompleto(
                    context = context,
                    backupUri = backupUri
                )

                _uiState.value = _uiState.value.copy(
                    processando = false,
                    mensagemSucesso = "Backup restaurado com sucesso. Feche e abra o aplicativo novamente para recarregar os dados.",
                    mensagemErro = null,
                    restauracaoConcluida = true
                )
            } catch (erro: Exception) {
                _uiState.value = _uiState.value.copy(
                    processando = false,
                    mensagemSucesso = null,
                    mensagemErro = erro.message ?: "Erro ao restaurar backup.",
                    restauracaoConcluida = false
                )
            }
        }
    }

    fun limparMensagens() {
        _uiState.value = _uiState.value.copy(
            mensagemSucesso = null,
            mensagemErro = null
        )
    }
}
