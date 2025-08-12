package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import com.cz.equiconti.data.Owner
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {

        // Lista proprietari
        composable("owners") {
            val vm: OwnersViewModel = hiltViewModel()
            val owners = vm.owners.collectAsState(emptyList()).value

            OwnersScreen(
                owners = owners,
                onOwnerClick = { ownerId ->
                    navController.navigate("owner/$ownerId")
                },
                onAddOwner = {
                    navController.navigate("addOwner")
                }
            )
        }

        // Aggiungi proprietario
        composable("addOwner") {
            AddOwnerScreen(
                nav = navController,
                onSave = { owner: Owner ->
                    val vm: OwnersViewModel = hiltViewModel()
                    vm.save(owner)
                    navController.popBackStack() // torna alla lista
                }
            )
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId")
                ?: run {
                    navController.popBackStack()
                    return@composable
                }

            OwnerDetailScreen(
                onBack = { navController.popBackStack() },
                onAddHorse = {
                    // Se/Quando avrai una schermata cavalli:
                    // navController.navigate("horses/$ownerId/add")
                }
            )
        }
    }
}
