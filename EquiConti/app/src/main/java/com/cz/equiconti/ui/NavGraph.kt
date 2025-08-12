package com.cz.equiconti.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen

/** Rotte centralizzate */
object Destinations {
    const val OWNERS = "owners"
    const val ADD_OWNER = "addOwner"
    const val EDIT_OWNER = "editOwner/{ownerId}"
    const val OWNER_DETAIL = "ownerDetail/{ownerId}"

    fun editOwner(ownerId: Long) = "editOwner/$ownerId"
    fun ownerDetail(ownerId: Long) = "ownerDetail/$ownerId"
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
            // Assumo che tu abbia OwnersScreen(nav: NavController)
            OwnersScreen(nav = navController)
        }

        // Aggiungi nuovo proprietario
        composable(Destinations.ADD_OWNER) {
            AddOwnerScreen(nav = navController, ownerId = null)
        }

        // Modifica proprietario esistente (riuso AddOwnerScreen passando l'id)
        composable(
            route = Destinations.EDIT_OWNER,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            AddOwnerScreen(nav = navController, ownerId = ownerId)
        }

        // Dettaglio proprietario
        composable(
            route = Destinations.OWNER_DETAIL,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(nav = navController, ownerId = ownerId)
        }
    }
}
