package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias

import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.CategoriaBenfeitoria

data class BenfeitoriasUiState(
    val benfeitorias: List<BenfeitoriaEntity> = emptyList(),
    val benfeitoriaId: Long? = null,
    val categoriaSelecionada: CategoriaBenfeitoria = CategoriaBenfeitoria.REPRODUTIVA,
    val nome: String = "",
    val descricao: String = "",
    val quantidade: String = "",
    val unidade: String = "",
    val estadoConservacao: String = "",
    val idadeAproximada: String = "",
    val observacao: String = "",
    val carregando: Boolean = false,
    val salvando: Boolean = false,
    val mensagemErro: String? = null
) {
    val editando: Boolean
        get() = benfeitoriaId != null
}
