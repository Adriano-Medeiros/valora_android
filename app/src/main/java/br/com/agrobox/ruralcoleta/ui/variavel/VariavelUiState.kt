package br.com.agrobox.ruralcoleta.ui.variavel

import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity
import br.com.agrobox.ruralcoleta.data.local.entity.TipoCampo
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity

data class VariavelUiState(
    val variaveis: List<VariavelEntity> = emptyList(),
    val grupos: List<GrupoVariavelEntity> = emptyList(),

    val variavelId: Long? = null,
    val nome: String = "",
    val tipoCampo: TipoCampo = TipoCampo.TEXTO,
    val unidade: String = "",
    val grupoId: Long? = null,
    val obrigatoria: Boolean = false,
    val dica: String = "",
    val ativo: Boolean = true,

    val carregando: Boolean = false,
    val salvando: Boolean = false,
    val mensagemErro: String? = null
) {
    val editando: Boolean
        get() = variavelId != null
}
