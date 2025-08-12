package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen

object Destinations {
    const val OWNERS = "owners"
    const val ADD_OWNER = "addOwner"
    const val OWNER_DETAIL = "ownerDetail"
    fun ownerDetail(ownerId: Long) = "$OWNER_DETAIL/$ownerId"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.OWNERS
    ) {
        // Lista proprietari
        composable(Destinations.OWNERS) {
            OwnersScreen(
                onOwnerClick = { ownerId ->
                    navController.navigate(Destinations.ownerDetail(ownerId))
                },
                onAddOwner = {
                    navController.navigate(Destinations.ADD_OWNER)
                }
            )
        }

        // Aggiungi/Modifica proprietario
        composable(Destinations.ADD_OWNER) {
            AddOwnerScreen(
                nav = navController,
                onSave = {
                    // La schermata salva via ViewModel; qui basta tornare indietro
                    navController.popBackStack()
                }
            )
        }

        // Dettaglio proprietario
        composable(
            route = "${Destinations.OWNER_DETAIL}/{ownerId}",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                id = ownerId,
                nav = navController
            )
        }
    }
}
