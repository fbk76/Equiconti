package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddHorseScreen
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.txn.TxnScreen

/**
 * Grafo di navigazione principale.
 * Rotte:
 *  - owners                     (lista proprietari)
 *  - owner/add                  (aggiungi proprietario)
 *  - owner/{ownerId}            (dettaglio proprietario)
 *  - owner/{ownerId}/addHorse   (aggiungi cavallo)
 *  - owner/{ownerId}/txns       (movimenti proprietario)
 */
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {
        // Lista proprietari
        composable("owners") {
            // OwnersScreen usa Hilt per il suo ViewModel internamente
            OwnersScreen(nav = navController)
        }

        // Aggiungi proprietario
        composable("owner/add") {
            // Schermata di aggiunta; quando termina, torna indietro
            AddOwnerScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                onBack = { navController.popBackStack() },
                onAddHorse = { navController.navigate("owner/$ownerId/addHorse") },
                onOpenTxns = { navController.navigate("owner/$ownerId/txns") },
                ownerId = ownerId
            )
        }

        // Aggiungi cavallo per proprietario
        composable(
            route = "owner/{ownerId}/addHorse",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            AddHorseScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() }
            )
        }

        // Movimenti (entrate/uscite) del proprietario
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            // la nostra TxnScreen espone onSave opzionale; per ora navBack dopo Salva
            TxnScreen(
                nav = navController,
                ownerId = ownerId,
                onSave = { _, _, _ -> /* collega al Repo/VM quando vuoi */ }
            )
        }
    }
}
