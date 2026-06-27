package br.com.agrobox.ruralcoleta.ui.modelo

import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity

data class ModeloColetaUiState(
    val modelos: List<ModeloColetaEntity> = emptyList(),
    val variaveis: List<VariavelEntity> = emptyList(),

    val modeloId: Long? = null,
    val nome: String = "",
    val descricao: String = "",
    val ativo: Boolean = true,

    val variaveisSelecionadas: List<Long> = emptyList(),

    val carregando: Boolean = false,
    val salvando: Boolean = false,
    val mensagemErro: String? = null
) {
    val editando: Boolean
        get() = modeloId != null
}