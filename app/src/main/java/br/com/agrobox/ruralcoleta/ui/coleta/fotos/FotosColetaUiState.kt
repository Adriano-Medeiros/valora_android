package br.com.agrobox.ruralcoleta.ui.coleta.fotos

import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity

data class FotosColetaUiState(
    val fotos: List<FotoColetaEntity> = emptyList(),
    val legenda: String = ""
)