package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddHorseScreen
// ATTENZIONE: gli altri screen (OwnersScreen, AddOwnerScreen, OwnerDetailScreen, TxnScreen)
// sono nello STESSO package di NavGraph (com.cz.equiconti.ui), quindi NON servono import.

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
        composable("addOwner") {
            AddOwnerScreen(
                nav = navController,
                onSave = { navController.popBackStack() }
            )
        }

        // Dettaglio proprietario
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

        // Aggiungi cavallo
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

        // Movimenti
        composable(
            route = "txns/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            TxnScreen(nav = navController, ownerId = ownerId)
        }
    }
}
