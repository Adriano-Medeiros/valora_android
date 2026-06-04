package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.fotos

import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity

data class FotosBenfeitoriaUiState(
    val fotos: List<FotoBenfeitoriaEntity> = emptyList(),
    val legenda: String = ""
)