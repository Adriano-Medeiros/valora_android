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

    private val _periodoAtividadesRecentes = MutableStateFlow(
        prefs.getInt(CHAVE_PERIODO_RECENTES, 7)
    )
    val periodoAtividadesRecentes = _periodoAtividadesRecentes.asStateFlow()

    private val _tutorialPrimeiroAcessoConcluido = MutableStateFlow(
        prefs.getBoolean(CHAVE_TUTORIAL_PRIMEIRO_ACESSO_CONCLUIDO, false)
    )
    val tutorialPrimeiroAcessoConcluido =
        _tutorialPrimeiroAcessoConcluido.asStateFlow()

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

    fun alterarPeriodoAtividadesRecentes(
        dias: Int
    ) {
        val valor = when (dias) {
            7, 15, 30 -> dias
            else -> 7
        }

        prefs.edit()
            .putInt(CHAVE_PERIODO_RECENTES, valor)
            .apply()

        _periodoAtividadesRecentes.value = valor
    }

    fun marcarTutorialPrimeiroAcessoConcluido() {
        prefs.edit()
            .putBoolean(CHAVE_TUTORIAL_PRIMEIRO_ACESSO_CONCLUIDO, true)
            .apply()

        _tutorialPrimeiroAcessoConcluido.value = true
    }

    fun reiniciarTutorialPrimeiroAcesso() {
        prefs.edit()
            .putBoolean(CHAVE_TUTORIAL_PRIMEIRO_ACESSO_CONCLUIDO, false)
            .apply()

        _tutorialPrimeiroAcessoConcluido.value = false
    }

    companion object {
        private const val CHAVE_GPS_AUTOMATICO = "capturar_gps_automaticamente"
        private const val CHAVE_MOSTRAR_RASCUNHOS = "mostrar_rascunhos_dashboard"
        private const val CHAVE_PERIODO_RECENTES = "periodo_atividades_recentes"

        private const val CHAVE_TUTORIAL_PRIMEIRO_ACESSO_CONCLUIDO =
            "tutorial_primeiro_acesso_concluido"
    }
}