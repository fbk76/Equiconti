package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {
        composable("owners") {
            OwnersScreen(
                onOwnerClick = { id -> navController.navigate("owner/$id") },
                onAddClick = { navController.navigate("addOwner") }
            )
        }

        composable("addOwner") {
            AddOwnerScreen(
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("owner/{ownerId}") {
            OwnerDetailScreen(
                onBack = { navController.popBackStack() },
                onAddHorse = { /* eventuale azione */ }
            )
        }

        composable("report") {
            ReportScreen(onBack = { navController.popBackStack() })
        }

        composable("txns/{ownerId}") {
            val ownerId = it.arguments?.getString("ownerId")?.toLongOrNull() ?: 0L
            TxnScreen(nav = navController, ownerId = ownerId)
        }
    }
}
