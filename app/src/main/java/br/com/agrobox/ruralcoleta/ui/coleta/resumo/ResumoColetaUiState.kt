package br.com.agrobox.ruralcoleta.ui.coleta.resumo

import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity

data class ResumoColetaUiState(
    val coleta: ColetaEntity? = null,
    val respostas: List<RespostaColetaEntity> = emptyList(),
    val benfeitorias: List<BenfeitoriaEntity> = emptyList(),
    val fotos: List<FotoColetaEntity> = emptyList(),
    val pendencias: List<String> = emptyList(),
    val mensagemErro: String? = null
)