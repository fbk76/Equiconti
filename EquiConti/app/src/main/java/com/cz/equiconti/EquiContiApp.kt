package com.cz.equiconti

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.horse.HorsesScreen
import com.cz.equiconti.ui.txn.TxnScreen

private enum class Tab(val route: String, val label: String) {
    Owners("owners", "Proprietari"),
    Horses("horses", "Cavalli"),
    Txns("txns", "Movimenti")
}

@Composable
fun EquiContiApp() {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                Tab.values().forEach { tab ->
                    val selected = currentDest.isOnRoute(tab.route)
                    NavigationBarItem(
                        selected = selected,
                        onClick = { if (!selected) nav.navigate(tab.route) { popUpTo(nav.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } },
                        label = { Text(tab.label) },
                        icon = { /* puoi aggiungere icone se vuoi */ }
                    )
                }
            }
        }
    ) { inner ->
        Surface(modifier = androidx.compose.ui.Modifier.padding(inner)) {
            NavHost(
                navController = nav,
                startDestination = Tab.Owners.route
            ) {
                composable(Tab.Owners.route) { OwnersScreen() }
                composable(Tab.Horses.route) { HorsesScreen() }
                composable(Tab.Txns.route)   { TxnScreen() }
            }
        }
    }
}

private fun NavDestination?.isOnRoute(route: String): Boolean =
    this?.hierarchy?.any { it.route == route } == true
