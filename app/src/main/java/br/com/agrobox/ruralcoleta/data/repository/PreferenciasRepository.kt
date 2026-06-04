package br.com.agrobox.ruralcoleta.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferenciasRepository(
    context: Context
) {
    private val prefs = context.getSharedPreferences(
        "rural_coleta_preferencias",
        Context.MODE_PRIVATE
    )

    private val _capturarGpsAutomaticamente = MutableStateFlow(
        prefs.getBoolean(CHAVE_GPS_AUTOMATICO, false)
    )

    val capturarGpsAutomaticamente = _capturarGpsAutomaticamente.asStateFlow()

    private val _mostrarRascunhosDashboard = MutableStateFlow(
        prefs.getBoolean(CHAVE_MOSTRAR_RASCUNHOS, true)
    )

    val mostrarRascunhosDashboard = _mostrarRascunhosDashboard.asStateFlow()

    fun alterarCapturarGpsAutomaticamente(
        ativo: Boolean
    ) {
        prefs.edit()
            .putBoolean(CHAVE_GPS_AUTOMATICO, ativo)
            .apply()

        _capturarGpsAutomaticamente.value = ativo
    }

    fun alterarMostrarRascunhosDashboard(
        ativo: Boolean
    ) {
        prefs.edit()
            .putBoolean(CHAVE_MOSTRAR_RASCUNHOS, ativo)
            .apply()

        _mostrarRascunhosDashboard.value = ativo
    }

    companion object {
        private const val CHAVE_GPS_AUTOMATICO = "capturar_gps_automaticamente"
        private const val CHAVE_MOSTRAR_RASCUNHOS = "mostrar_rascunhos_dashboard"
    }
}