package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.horse.HorseEditFormScreen   // <-- usa il nuovo form
import com.cz.equiconti.ui.txn.TxnScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {
        // Lista proprietari
        composable("owners") {
            OwnersScreen(nav = navController)
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
                // nuova rotta "nuovo cavallo"
                onAddHorse = { navController.navigate("owner/$ownerId/horse/new") },
                onOpenTxns  = { navController.navigate("owner/$ownerId/txns") }
            )
        }

        // === CAVALLI ===

        // 1) Nuovo cavallo
        composable(
            route = "owner/{ownerId}/horse/new",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L

            HorseEditFormScreen(
                title = "Nuovo cavallo",
                initialName = "",
                onBack = { navController.popBackStack() },
                onSave = { name ->
                    // TODO: salva il cavallo nel tuo ViewModel/Repo (ownerId, name)
                    // es.: viewModel.addHorse(ownerId, name)
                    navController.popBackStack()
                }
            )
        }

        // 2) Modifica cavallo (facoltativo, lasciato pronto)
        composable(
            route = "owner/{ownerId}/horse/{horseId}/edit?name={name}",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType },
                navArgument("horseId") { type = NavType.LongType },
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val /* ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L */
                horseId = backStackEntry.arguments?.getLong("horseId") ?: 0L
            val initialName = backStackEntry.arguments?.getString("name") ?: ""

            HorseEditFormScreen(
                title = "Modifica cavallo",
                initialName = initialName,
                onBack = { navController.popBackStack() },
                onSave = { name ->
                    // TODO: aggiorna cavallo nel tuo ViewModel/Repo (horseId, name)
                    // es.: viewModel.updateHorse(horseId, name)
                    navController.popBackStack()
                }
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
