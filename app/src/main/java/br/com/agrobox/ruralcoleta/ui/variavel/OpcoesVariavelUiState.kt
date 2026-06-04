package br.com.agrobox.ruralcoleta.ui.variavel.opcoes

import br.com.agrobox.ruralcoleta.data.local.entity.OpcaoVariavelEntity

data class OpcoesVariavelUiState(
    val opcoes: List<OpcaoVariavelEntity> = emptyList(),
    val descricao: String = "",
    val ordem: String = "0"
)