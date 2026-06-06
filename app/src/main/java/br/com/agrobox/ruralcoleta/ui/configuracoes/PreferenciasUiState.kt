package br.com.agrobox.ruralcoleta.ui.configuracoes

data class PreferenciasUiState(
    val capturarGpsAutomaticamente: Boolean = false,
    val mostrarRascunhosDashboard: Boolean = true,
    val periodoAtividadesRecentes: Int = 7
)