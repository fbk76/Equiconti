package com.cz.equiconti.ui.nav

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

private object Routes {
    const val OWNERS = "owners"
    const val OWNER_NEW = "owner/new"
    const val OWNER_DETAIL = "owner/{ownerId}"
}

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.OWNERS
    ) {
        // Lista proprietari
        composable(Routes.OWNERS) {
            OwnersScreen(
                onOwnerClick = { ownerId: Long ->
                    navController.navigate("owner/$ownerId")
                },
                onAddOwner = {
                    navController.navigate(Routes.OWNER_NEW)
                }
            )
        }

        // Aggiungi proprietario
        composable(Routes.OWNER_NEW) {
            // Se ti serve salvare dal VM:
            val vm: OwnersViewModel = hiltViewModel()
            AddOwnerScreen(
                nav = navController,
                onSave = { owner ->
                    // se nel VM hai una funzione di salvataggio, usala:
                    // vm.save(owner)
                    // poi torna indietro alla lista
                    navController.popBackStack()
                }
            )
        }

        // Dettaglio proprietario (il composable legge l'ID dal SavedStateHandle del suo VM)
        composable(
            route = Routes.OWNER_DETAIL,
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) {
            OwnerDetailScreen(
                onBack = { navController.popBackStack() },
                onAddHorse = {
                    // qui in futuro potrai navigare alla schermata per aggiungere un cavallo
                }
            )
        }
    }
}
