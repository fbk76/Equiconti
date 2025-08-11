package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen

object Routes {
    const val Owners = "owners"
    const val AddOwner = "addOwner"
    const val OwnerDetail = "owner/{ownerId}"
    fun ownerDetail(ownerId: Long) = "owner/$ownerId"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Owners
    ) {
        // Lista proprietari
        composable(Routes.Owners) {
            OwnersScreen(
                onOwnerClick = { owner ->
                    navController.navigate(Routes.ownerDetail(owner.id))
                },
                onAddOwner = {
                    navController.navigate(Routes.AddOwner)
                }
            )
        }

        // Aggiungi/modifica proprietario
        composable(Routes.AddOwner) {
            AddOwnerScreen(
                nav = navController,
                onSave = {
                    // dopo il salvataggio torna alla lista
                    navController.popBackStack(Routes.Owners, inclusive = false)
                }
            )
        }

        // Dettaglio proprietario
        composable(
            route = Routes.OwnerDetail,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) {
            OwnerDetailScreen(
                onBack = { navController.popBackStack() },
                onAddHorse = { /* TODO: naviga a una schermata cavalli se/quando la aggiungiamo */ }
            )
        }
    }
}
