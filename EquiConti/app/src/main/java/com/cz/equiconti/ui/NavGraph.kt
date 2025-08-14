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
 * Rotte:
 *  - owners
 *  - owner/add
 *  - owner/{ownerId}
 *  - owner/{ownerId}/addHorse
 *  - owner/{ownerId}/txns
 */
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

        // Aggiungi proprietario
        composable("owner/add") {
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
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onAddHorse = { navController.navigate("owner/$ownerId/addHorse") },
                onOpenTxns = { navController.navigate("owner/$ownerId/txns") }
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
                // Se vuoi, puoi passare onSave = { horse -> vm.upsertHorse(horse) }
            )
        }

        // Movimenti (entrate/uscite) del proprietario
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            TxnScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onSave = { amount, isIncome, date, note ->
                    // 👉 Qui puoi collegare il salvataggio reale con il tuo ViewModel/Repo.
                    // Esempio (se hai un OwnersViewModel):
                    // val vm: OwnersViewModel = hiltViewModel()
                    // vm.addTxn(ownerId, amount, isIncome, date, note)
                    navController.popBackStack()
                }
            )
        }
    }
}
