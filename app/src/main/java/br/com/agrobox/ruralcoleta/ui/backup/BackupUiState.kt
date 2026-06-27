package br.com.agrobox.ruralcoleta.ui.backup

data class BackupUiState(
    val processando: Boolean = false,
    val mensagemSucesso: String? = null,
    val mensagemErro: String? = null,
    val restauracaoConcluida: Boolean = false
)
