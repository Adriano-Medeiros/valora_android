package br.com.agrobox.ruralcoleta.ui.coleta.detalhe

import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaComFotos
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaDetalhada

data class DetalheColetaUiState(
    val coleta: ColetaEntity? = null,
    val respostas: List<RespostaDetalhada> = emptyList(),
    val benfeitorias: List<BenfeitoriaComFotos> = emptyList(),
    val fotos: List<FotoColetaEntity> = emptyList(),
    val fotosPorBenfeitoria: Map<Long, List<FotoBenfeitoriaEntity>> = emptyMap()
)