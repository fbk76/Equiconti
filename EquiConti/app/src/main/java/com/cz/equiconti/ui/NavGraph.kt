package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // Lista proprietari
        composable("owners") {
            val owners = vm.owners.collectAsState().value
            OwnersScreen(
                owners = owners,
                onOwnerClick = { id -> navController.navigate("owner/$id") },
                onAddOwner = { navController.navigate("addOwner") }
            )
        }

        // Aggiunta proprietario
        composable("addOwner") {
            AddOwnerScreen(
                onSave = { owner ->
                    vm.saveOwner(owner)
                    navController.popBackStack() // torna alla lista
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: return@composable

            // carica il proprietario selezionato nel VM e osservalo
            LaunchedEffect(id) { vm.loadOwner(id) }
            val owner = vm.owner.collectAsState().value

            owner?.let { current ->
                OwnerDetailScreen(
                    owner = current,
                    onBack = { navController.popBackStack() },
                    onDelete = {
                        vm.removeOwner(current.id)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
