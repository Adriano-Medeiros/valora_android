package br.com.agrobox.ruralcoleta

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.agrobox.ruralcoleta.data.local.database.DatabaseProvider
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoBenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.GrupoVariavelRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.OpcaoVariavelRepository
import br.com.agrobox.ruralcoleta.data.repository.PreferenciasRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.VariavelRepository
import br.com.agrobox.ruralcoleta.ui.navigation.AppNavigation
import br.com.agrobox.ruralcoleta.ui.theme.RuralColetaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(
                Color.WHITE,
                Color.WHITE
            )
        )

        val database = DatabaseProvider.getDatabase(applicationContext)

        val coletaRepository = ColetaRepository(
            dao = database.coletaDao()
        )

        val grupoVariavelRepository = GrupoVariavelRepository(
            dao = database.grupoVariavelDao()
        )
        val variavelRepository = VariavelRepository(
            dao = database.variavelDao()
        )
        val modeloColetaRepository = ModeloColetaRepository(
            modeloDao = database.modeloColetaDao(),
            modeloVariavelDao = database.modeloColetaVariavelDao()
        )
        val respostaColetaRepository = RespostaColetaRepository(
            dao = database.respostaColetaDao()
        )
        val benfeitoriaRepository = BenfeitoriaRepository(
            dao = database.benfeitoriaDao()
        )
        val fotoColetaRepository = FotoColetaRepository(
            dao = database.fotoColetaDao()
        )
        val fotoBenfeitoriaRepository = FotoBenfeitoriaRepository(
            dao = database.fotoBenfeitoriaDao()
        )
        val opcaoVariavelRepository = OpcaoVariavelRepository(
            dao = database.opcaoVariavelDao()
        )
        val preferenciasRepository = PreferenciasRepository(
            context = this
        )
        setContent {
            RuralColetaTheme {
                AppNavigation(
                    coletaRepository = coletaRepository,
                    grupoVariavelRepository = grupoVariavelRepository,
                    variavelRepository = variavelRepository,
                    modeloColetaRepository = modeloColetaRepository,
                    respostaColetaRepository = respostaColetaRepository,
                    benfeitoriaRepository = benfeitoriaRepository,
                    fotoColetaRepository = fotoColetaRepository,
                    fotoBenfeitoriaRepository = fotoBenfeitoriaRepository,
                    opcaoVariavelRepository = opcaoVariavelRepository,
                    preferenciasRepository = preferenciasRepository,
                )
            }
        }
    }
}