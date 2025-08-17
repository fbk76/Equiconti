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
                // NB: per aprire i movimenti ora devi passare un horseId: vedi nota sotto.
                onOpenTxns = { /* placeholder, vedi nota sotto */ },
                nav = navController
            )
        }

        // Aggiunta cavallo per un proprietario
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

        // Movimenti per UN cavallo specifico
        composable(
            route = "horse/{horseId}/txns",
            arguments = listOf(navArgument("horseId") { type = NavType.LongType })
        ) { backStackEntry ->
            val horseId = backStackEntry.arguments?.getLong("horseId") ?: 0L
            TxnScreen(
                horseId = horseId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
