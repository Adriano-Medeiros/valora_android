package br.com.agrobox.ruralcoleta.ui.coleta.formulario

import br.com.agrobox.ruralcoleta.data.local.entity.OpcaoVariavelEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity

data class FormularioDinamicoUiState(
    val coletaId: Long = 0,
    val modeloColetaId: Long? = null,
    val variaveis: List<VariavelEntity> = emptyList(),
    val opcoesPorVariavel: Map<Long, List<OpcaoVariavelEntity>> = emptyMap(),
    val respostas: Map<Long, String> = emptyMap(),
    val camposComErro: Set<Long> = emptySet(),
    val mensagemErro: String? = null,
    val salvando: Boolean = false
)