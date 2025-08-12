package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "owners",
        modifier = modifier
    ) {
        // Lista proprietari
        composable("owners") {
            val vm: OwnersViewModel = hiltViewModel()
            OwnersScreen(
                navController = navController,
                vm = vm
            )
        }

        // Aggiunta proprietario
        composable("addOwner") {
            val vm: OwnersViewModel = hiltViewModel()
            AddOwnerScreen(
                navController = navController,
                vm = vm
            )
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val vm: OwnersViewModel = hiltViewModel()
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onDelete = { navController.popBackStack() },
                vm = vm
            )
        }
    }
}
