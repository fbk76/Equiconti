package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

object Routes {
    const val Owners = "owners"
    const val AddOwner = "owner/add"
    const val OwnerDetail = "owner/{ownerId}"
    fun ownerDetail(id: Long) = "owner/$id"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    // ViewModel condiviso per la lista proprietari
    val vm: OwnersViewModel = hiltViewModel()
    val owners by vm.owners.collectAsState()
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Routes.Owners
    ) {
        // Lista proprietari
        composable(Routes.Owners) {
            OwnersScreen(
                owners = owners,
                onAddOwner = { navController.navigate(Routes.AddOwner) },
                onOwnerClick = { id -> navController.navigate(Routes.ownerDetail(id)) }
            )
        }

        // Aggiungi/Modifica proprietario
        composable(Routes.AddOwner) {
            AddOwnerScreen(
                onSave = { owner ->
                    scope.launch {
                        vm.upsert(owner)
                        navController.popBackStack() // torna alla lista
                    }
                }
            )
        }

        // Dettaglio proprietario
        composable(
            route = Routes.OwnerDetail,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
