package br.com.agrobox.ruralcoleta.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    onPrimary = Branco,

    secondary = VerdeClaro,
    onSecondary = VerdeEscuro,

    background = FundoClaro,
    onBackground = TextoPrincipal,

    surface = Branco,
    onSurface = TextoPrincipal,

    error = Color(0xFFB3261E),
    onError = Branco
)

@Composable
fun RuralColetaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = VerdeEscuro.toArgb()
            window.navigationBarColor = Branco.toArgb()

            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = false

            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}