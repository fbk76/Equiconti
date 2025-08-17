package com.cz.equiconti.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.owner.OwnersRootScreen
import com.cz.equiconti.ui.owner.horse.HorsesScreen
import com.cz.equiconti.ui.txn.TxnScreen

private object Routes {
    const val OWNERS = "owners"
    const val HORSES = "horses"
    const val TXNS   = "txns"
}

data class BottomTab(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun HomeScaffold() {
    val navController = rememberNavController()

    val tabs = listOf(
        BottomTab(Routes.OWNERS, "Proprietari", Icons.Filled.AccountBox),
        BottomTab(Routes.HORSES, "Cavalli",     Icons.Filled.Pets),
        BottomTab(Routes.TXNS,   "Movimenti",   Icons.Filled.History),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination

                tabs.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.OWNERS
        ) {
            // Root proprietari (usa la tua schermata reale, vedi sotto)
            composable(Routes.OWNERS) {
                OwnersRootScreen(innerPadding = innerPadding)
            }
            composable(Routes.HORSES) {
                HorsesScreen(innerPadding = innerPadding)
            }
            composable(Routes.TXNS) {
                TxnScreen(innerPadding = innerPadding)
            }
        }
    }
}
