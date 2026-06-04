package br.com.agrobox.ruralcoleta.ui.grupo

import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity

data class GrupoVariavelUiState(
    val grupos: List<GrupoVariavelEntity> = emptyList(),
    val nome: String = "",
    val descricao: String = "",
    val ordem: String = "0",
    val ativo: Boolean = true,
    val salvando: Boolean = false
)