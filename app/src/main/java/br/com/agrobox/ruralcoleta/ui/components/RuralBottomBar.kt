package br.com.agrobox.ruralcoleta.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.agrobox.ruralcoleta.ui.navigation.bottomNavItems

@Composable
fun RuralBottomBar(
    navController: NavController
) {
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    NavigationBar(
        containerColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00823B),
                    selectedTextColor = Color(0xFF00823B),
                    indicatorColor = Color(0xFFE1F5E8)
                )
            )
        }
    }
}