package com.cz.equiconti

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private enum class Tab(val route: String, val label: String) {
    Owners("owners", "Proprietari"),
    Horses("horses", "Cavalli"),
    Txns("txns", "Movimenti")
}

/**
 * Entry point UI semplice e robusto:
 * - Bottom bar con 3 tab
 * - Ogni tab mostra una schermata base (senza dipendenze da vecchi composable con parametri)
 * Così evitiamo tutti gli errori tipo “No value passed for parameter …”.
 */
@Composable
fun EquiContiApp() {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    Tab.values().forEach { tab ->
                        val selected = currentDest.isOnRoute(tab.route)
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    nav.navigate(tab.route) {
                                        popUpTo(nav.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            label = { Text(tab.label) },
                            icon = { /* puoi aggiungere icone più avanti */ }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                NavHost(
                    navController = nav,
                    startDestination = Tab.Owners.route
                ) {
                    composable(Tab.Owners.route) { OwnersHomeScreen() }
                    composable(Tab.Horses.route) { HorsesHomeScreen() }
                    composable(Tab.Txns.route)   { TxnsHomeScreen() }
                }
            }
        }
    }
}

private fun NavDestination?.isOnRoute(route: String): Boolean =
    this?.hierarchy?.any { it.route == route } == true

/* -------------------------------------------------------------------------- */
/*  Schermate base: niente parametri, niente VM obbligatori → niente errori   */
/*  (potrai arricchirle in seguito collegandole ai tuoi ViewModel/DAO)        */
/* -------------------------------------------------------------------------- */

@Composable
private fun OwnersHomeScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text("Proprietari — nessun dato da mostrare (DB vuoto)", modifier = Modifier.padding(16))
    }
}

@Composable
private fun HorsesHomeScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text("Cavalli — nessun dato da mostrare (DB vuoto)", modifier = Modifier.padding(16))
    }
}

@Composable
private fun TxnsHomeScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text("Movimenti — nessun dato da mostrare (DB vuoto)", modifier = Modifier.padding(16))
    }
}
