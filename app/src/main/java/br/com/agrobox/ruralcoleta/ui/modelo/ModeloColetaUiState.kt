package br.com.agrobox.ruralcoleta.ui.modelo

import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity

data class ModeloColetaUiState(
    val modelos: List<ModeloColetaEntity> = emptyList(),
    val variaveis: List<VariavelEntity> = emptyList(),

    val nome: String = "",
    val descricao: String = "",
    val ativo: Boolean = true,

    val variaveisSelecionadas: Set<Long> = emptySet(),

    val salvando: Boolean = false
)