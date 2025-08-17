package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.horse.HorseAddScreen
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
                nav = navController,
                onOwnerClick = { ownerId ->
                    // apre il dettaglio del proprietario selezionato
                    navController.navigate("owner/$ownerId")
                },
                onAddOwner = {
                    // apre la schermata di dettaglio in modalità "nuovo".
                    // Se il tuo OwnerDetailScreen gestisce ownerId=0 come "nuovo", va bene così.
                    // In caso contrario, dimmelo e ti preparo una rotta dedicata.
                    navController.navigate("owner/0")
                }
            )
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onOpenTxns = { navController.navigate("owner/$ownerId/txns") },
                nav = navController
            )
        }

        // Aggiunta cavallo
        composable(
            route = "owner/{ownerId}/addHorse",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            HorseAddScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() }
            )
        }

        // Movimenti del proprietario
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            TxnScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
