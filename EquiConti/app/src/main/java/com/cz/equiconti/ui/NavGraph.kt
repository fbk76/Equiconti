package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.HorsesScreen
import com.cz.equiconti.ui.owner.TxnScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "owners") {

        composable("owners") {
            val vm = hiltViewModel<OwnersViewModel>()
            OwnersScreen(
                owners = vm.owners,
                onOwnerClick = { id -> nav.navigate("owner/$id") },
                onAddOwner = { nav.navigate("owner/new") }
            )
        }

        composable("owner/new") {
            val vm = hiltViewModel<OwnersViewModel>()
            AddOwnerScreen(
                onSave = { owner ->
                    vm.upsertOwner(owner)
                    nav.popBackStack()
                },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "owner/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val vm = hiltViewModel<OwnersViewModel>()
            val id = backStack.arguments?.getLong("id") ?: 0L
            OwnerDetailScreen(
                ownerFlow = vm.ownerFlow(id),
                onBack = { nav.popBackStack() },
                onEdit = { nav.navigate("owner/new") }, // semplice: riusa form “new” per ora
                onDelete = {
                    vm.deleteOwner(it)
                    nav.popBackStack()
                },
                onOpenHorses = { nav.navigate("owner/$id/horses") },
                onOpenTxns = { nav.navigate("owner/$id/txns") }
            )
        }

        composable(
            route = "owner/{id}/horses",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val vm = hiltViewModel<OwnersViewModel>()
            val ownerId = backStack.arguments?.getLong("id") ?: 0L
            HorsesScreen(
                horsesFlow = vm.horses(ownerId),
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "owner/{id}/txns",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val vm = hiltViewModel<OwnersViewModel>()
            val ownerId = backStack.arguments?.getLong("id") ?: 0L
            TxnScreen(
                txnsFlow = vm.txns(ownerId),
                onBack = { nav.popBackStack() }
            )
        }
    }
}
