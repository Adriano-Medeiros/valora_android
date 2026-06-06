package br.com.agrobox.ruralcoleta.ui.dashboard

import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity

data class DashboardUiState(
    val coletas: List<ColetaEntity> = emptyList(),
    val totalColetas: Int = 0,
    val totalRascunhos: Int = 0,
    val totalConcluidas: Int = 0,
    val totalAvaliando: Int = 0,
    val totalAmostral: Int = 0,
    val totalRecentes: Int = 0,
    val periodoRecentesDias: Int = 7
)