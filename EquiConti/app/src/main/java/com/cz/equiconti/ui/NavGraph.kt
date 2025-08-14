package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.HorsesScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.txn.TxnScreen
import com.cz.equiconti.ui.owner.OwnersViewModel   // il tuo VM principale (adattalo se si chiama diverso)

@Composable
fun NavGraph(navController: NavHostController) {
    val vm = hiltViewModel<OwnersViewModel>()

    NavHost(navController = navController, startDestination = "owners") {

        composable("owners") {
            val owners by vm.owners.collectAsState(initial = emptyList()) // Flow<List<Owner>>
            OwnersScreen(
                owners = owners,
                onAddOwner = { ln, fn -> vm.upsertOwner(ln, fn) }, // <-- implementa in VM
                onOpenOwner = { id -> navController.navigate("owner/$id") }
            )
        }

        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            val owner by vm.owner(ownerId).collectAsState(initial = null)  // <-- implementa in VM
            val horses by vm.horses(ownerId).collectAsState(initial = emptyList()) // <-- implementa in VM

            val ownerName = owner?.let { "${it.lastName} ${it.firstName}" } ?: "Proprietario"

            HorsesScreen(
                ownerName = ownerName,
                horses = horses,
                onBack = { navController.popBackStack() },
                onAddHorse = { name, amountCents -> vm.upsertHorse(ownerId, name, amountCents) } // <-- implementa in VM
            )
        }

        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            val owner by vm.owner(ownerId).collectAsState(initial = null)
            val horses by vm.horses(ownerId).collectAsState(initial = emptyList())
            val txns by vm.txns(ownerId).collectAsState(initial = emptyList()) // <-- implementa in VM

            val ownerName = owner?.let { "${it.lastName} ${it.firstName}" } ?: "#$ownerId"
            val horseNames = horses.map { it.name }

            TxnScreen(
                ownerName = ownerName,
                ownerHorses = horseNames,
                txns = txns,
                onAddTxn = { dateMs, op, inc, exp -> vm.insertTxn(ownerId, dateMs, op, inc, exp) } // <-- implementa in VM
            )
        }
    }
}
