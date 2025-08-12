package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = "owners"
) {
    val vm: OwnersViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("owners") {
            OwnersScreen(
                owners = vm.owners,
                onOwnerClick = { id -> navController.navigate("owner/$id") },
                onAddOwner = { navController.navigate("addOwner") }
            )
        }

        composable("addOwner") {
            AddOwnerScreen(
                onSave = { owner ->
                    vm.saveOwner(owner)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: return@composable

            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onDelete = {
                    vm.removeOwner(ownerId)
                    navController.popBackStack()
                }
            )
        }
    }
}
