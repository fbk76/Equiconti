package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.data.Owner
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

/**
 * Rotte usate nell’app
 */
private object Routes {
    const val OWNERS = "owners"
    const val OWNER_ADD = "owner/add"
    const val OWNER_DETAIL = "owner/{ownerId}"
    fun ownerDetail(ownerId: Long) = "owner/$ownerId"
}

@Composable
fun AppNavGraph(
    navController: NavController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.OWNERS
    ) {
        // Lista proprietari
        composable(Routes.OWNERS) {
            OwnersScreen(
                onOwnerClick = { id ->
                    navController.navigate(Routes.ownerDetail(id))
                },
                onAddOwner = {
                    navController.navigate(Routes.OWNER_ADD)
                }
            )
        }

        // Aggiunta/edizione proprietario
        composable(Routes.OWNER_ADD) {
            val vm: OwnersViewModel = hiltViewModel()

            AddOwnerScreen(
                nav = navController,
                onSave = { owner: Owner ->
                    // Chiama il metodo del ViewModel e torna indietro.
                    // Assicurati che OwnersViewModel esponga questa funzione.
                    vm.upsertOwner(owner)
                    navController.popBackStack()
                }
            )
        }

        // Dettaglio proprietario
        composable(
            route = Routes.OWNER_DETAIL,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                onBack = { navController.popBackStack() },
                onAddHorse = {
                    // qui, se/quando servirà: navController.navigate("horse/add/$ownerId")
                }
            )
        }
    }
}
