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
                onOwnerClick = { ownerId ->
                    navController.navigate("owner/$ownerId")
                },
                onAddOwner = {
                    // esempio: se avrai uno screen dedicato aggiungilo qui
                    // navController.navigate("addOwner")
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
                onOpenTxns = { navController.navigate("owner/$ownerId/txns") }
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
                horseId = ownerId, // ⚠️ se in realtà serviva horseId cambia qui!
                onBack = { navController.popBackStack() }
            )
        }
    }
}
