package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.txn.TxnScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {
        // Schermata elenco proprietari
        composable("owners") {
            OwnersScreen(navController)
        }

        // Schermata dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(navController, ownerId)
        }

        // Schermata movimenti proprietario
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            TxnScreen(navController, ownerId)
        }

        // Puoi aggiungere qui altre destinazioni
    }
}
