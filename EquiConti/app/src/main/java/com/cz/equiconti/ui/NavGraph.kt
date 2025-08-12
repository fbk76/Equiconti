package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
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

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    val vm: OwnersViewModel = hiltViewModel()

    val owners by vm.owners.collectAsState()

    NavHost(
        navController = nav,
        startDestination = "owners"
    ) {
        // Lista proprietari
        composable("owners") {
            OwnersScreen(
                owners = owners,
                onAddOwner = { nav.navigate("addOwner") },
                onOwnerClick = { ownerId -> nav.navigate("owner/$ownerId") }
            )
        }

        // Aggiunta / modifica proprietario (creazione)
        composable("addOwner") {
            AddOwnerScreen(
                onSave = { owner: Owner ->
                    vm.upsertOwner(owner)
                    nav.popBackStack()
                },
                onBack = { nav.popBackStack() }
            )
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            val owner by vm.owner(ownerId).collectAsState(initial = null)

            OwnerDetailScreen(
                ownerId = ownerId,
                owner = owner,                // se il tuo screen non lo prevede, puoi rimuovere questa riga
                onBack = { nav.popBackStack() },
                onDelete = {
                    vm.deleteOwnerById(ownerId)
                    nav.popBackStack()
                },
                onSave = { updated ->
                    vm.upsertOwner(updated)
                    nav.popBackStack()
                }
            )
        }
    }
}
