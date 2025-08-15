package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.data.Txn
import com.cz.equiconti.ui.horse.HorseEditScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersListScreen
import com.cz.equiconti.ui.owner.OwnersViewModel
import com.cz.equiconti.ui.txn.TxnScreen

/**
 * Grafico di navigazione principale.
 * La funzione si chiama NavGraph per combaciare con MainActivity.
 */
@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val nav = rememberNavController()
    val vm: OwnersViewModel = hiltViewModel()

    NavHost(
        navController = nav,
        startDestination = "owners",
        modifier = modifier
    ) {
        // Lista proprietari
        composable("owners") {
            val owners = vm.owners.collectAsState(initial = emptyList()).value
            OwnersListScreen(
                owners = owners,
                onAddOwner = { nav.navigate("owner/new") },
                onOpenOwner = { owner ->
                    nav.navigate("owner/${owner.id}")
                }
            )
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) { backStack ->
            val ownerId = backStack.arguments?.getLong("ownerId")
                ?: error("ownerId missing")

            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onAddHorse = { nav.navigate("owner/$ownerId/addHorse") },
                onOpenTxns = { nav.navigate("owner/$ownerId/txns") }
            )
        }

        // Aggiungi / modifica cavallo
        composable(
            route = "owner/{ownerId}/addHorse",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) { backStack ->
            val ownerId = backStack.arguments?.getLong("ownerId")
                ?: error("ownerId missing")

            HorseEditScreen(
                ownerId = ownerId,
                onCancel = { nav.popBackStack() },
                onSaved = { nav.popBackStack() }
            )
        }

        // Movimenti del proprietario
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) { backStack ->
            val ownerId = backStack.arguments?.getLong("ownerId")
                ?: error("ownerId missing")

            val owners = vm.owners.collectAsState(initial = emptyList()).value
            val owner = owners.firstOrNull { it.id == ownerId }
            val horses = vm.horses(ownerId).collectAsState(initial = emptyList()).value
            val txns = vm.txns(ownerId).collectAsState(initial = emptyList()).value

            TxnScreen(
                ownerName = owner?.let { "${it.lastName} ${it.firstName}" } ?: "Proprietario",
                ownerHorses = horses.map { it.name },
                txns = txns,
                onAddTxn = { dateMs, operation, incomeCents, expenseCents ->
                    vm.upsertTxn(
                        // ORDINE CORRETTO: operation (String) PRIMA di dateMs (Long)
                        Txn(
                            0L,          // id/txnId autogenerato
                            ownerId,     // ownerId
                            operation,   // operation (String)
                            dateMs,      // dateMillis (Long)
                            incomeCents, // incomeCents
                            expenseCents // expenseCents
                        )
                    )
                }
            )
        }
    }
}
