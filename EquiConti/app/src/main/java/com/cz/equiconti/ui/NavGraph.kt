package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddHorseScreen
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.txn.TxnScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {
        // Lista proprietari
        composable("owners") {
            OwnersScreen(
                onAddOwner = { navController.navigate("addOwner") },
                onOwnerClick = { ownerId ->
                    navController.navigate("ownerDetail/$ownerId")
                }
            )
        }

        // Aggiungi proprietario  — firma: (nav: NavController, onSave: (Owner) -> Unit)
        composable("addOwner") {
            AddOwnerScreen(
                nav = navController,
                onSave = { /* dopo il salvataggio torniamo alla lista */
                    navController.popBackStack()
                }
            )
        }

        // Dettaglio proprietario — firma: (onBack: () -> Unit, onAddHorse: () -> Unit = {})
        composable(
            route = "ownerDetail/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                onBack = { navController.popBackStack() },
                onAddHorse = { navController.navigate("addHorse/$ownerId") }
            )
        }

        // Aggiungi cavallo — firma: (ownerId: Long, onBack: () -> Unit)
        composable(
            route = "addHorse/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            AddHorseScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() }
            )
        }

        // Movimenti — firma: (nav: NavController, ownerId: Long)
        composable(
            route = "txns/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            TxnScreen(nav = navController, ownerId = ownerId)
        }
    }
}
