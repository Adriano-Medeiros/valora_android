package br.com.agrobox.ruralcoleta.ui.exportacao

import br.com.agrobox.ruralcoleta.domain.exportacao.TipoExportacao

data class ExportacaoUiState(
    val exportando: Boolean = false,
    val tipoExportando: TipoExportacao? = null,
    val mensagemErro: String? = null
)
