package br.com.agrobox.ruralcoleta.ui.exportacao

import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity

data class ExportacaoColetasUiState(
    val coletas: List<ColetaEntity> = emptyList(),
    val selecionadas: Set<Long> = emptySet()
)