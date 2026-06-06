package br.com.agrobox.ruralcoleta.ui.mapa

import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity

data class MapaColetasUiState(
    val coletas: List<ColetaEntity> = emptyList(),
    val coletasFiltradas: List<ColetaEntity> = emptyList(),
    val mostrarAvaliando: Boolean = true,
    val mostrarAmostral: Boolean = true,
    val mostrarRascunho: Boolean = true,
    val mostrarConcluida: Boolean = true
)