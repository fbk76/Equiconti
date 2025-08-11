package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cz.equiconti.data.Owner
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.txn.TxnScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

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
                onOwnerClick = { ownerId -> navController.navigate("owner/$ownerId") }
            )
        }

        // Aggiungi/Modifica proprietario
        composable("addOwner") {
            val vm: OwnersViewModel = hiltViewModel()

            AddOwnerScreen(
                nav = navController,
                onSave = { owner: Owner ->
                    // delego al ViewModel e torno indietro
                    vm.save(owner)
                    navController.popBackStack()
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
                onBack = { navController.popBackStack() },
                onAddHorse = { /* se in futuro servirà: navController.navigate("owner/$ownerId/addHorse") */ }
            )
        }

        // Movimenti del proprietario
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            TxnScreen(nav = navController, ownerId = ownerId)
        }
    }
}
