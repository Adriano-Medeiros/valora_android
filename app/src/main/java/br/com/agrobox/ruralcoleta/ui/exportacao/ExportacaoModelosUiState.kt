package br.com.agrobox.ruralcoleta.ui.exportacao

import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity

data class ExportacaoModelosUiState(
    val modelos: List<ModeloColetaEntity> = emptyList()
)