package br.com.agrobox.ruralcoleta.ui.grupo

import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity

data class GrupoVariavelUiState(
    val grupos: List<GrupoVariavelEntity> = emptyList(),
    val grupoId: Long? = null,
    val nome: String = "",
    val descricao: String = "",
    val ordem: String = "0",
    val ativo: Boolean = true,
    val carregando: Boolean = false,
    val salvando: Boolean = false,
    val mensagemErro: String? = null
) {
    val editando: Boolean
        get() = grupoId != null
}
