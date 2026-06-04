package br.com.agrobox.ruralcoleta.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.ListAlt
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Dashboard", Icons.Default.Home, Screen.Dashboard.route),
    BottomNavItem("Coletas", Icons.Default.ListAlt, Screen.Coletas.route),
    BottomNavItem("Novo", Icons.Default.AddCircle, Screen.NovaColeta.route),
    BottomNavItem("Mapa", Icons.Default.Map, "mapa"),
    BottomNavItem("Mais", Icons.Default.Menu, Screen.Configuracoes.route)
)