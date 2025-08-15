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
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersListScreen
import com.cz.equiconti.ui.owner.OwnersViewModel
import com.cz.equiconti.ui.horse.HorseEditScreen
import com.cz.equiconti.ui.txn.TxnScreen
import com.cz.equiconti.data.Txn

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
            val owners = vm.owners.collectAsState().value
            OwnersListScreen(
                owners = owners,
                onAddOwner = { nav.navigate("owner/new") },     // opzionale: schermata "nuovo"
                onOpenOwner = { owner ->
                    // apri dettaglio passando ownerId
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

        // Aggiungi/modifica cavallo
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

        // Movimenti proprietario
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) { backStack ->
            val ownerId = backStack.arguments?.getLong("ownerId")
                ?: error("ownerId missing")

            // Dati live
            val owner = vm.owners.collectAsState(emptyList()).value.firstOrNull { it.id == ownerId }
            val horses = vm.horses(ownerId).collectAsState(initial = emptyList()).value
            val txns = vm.txns(ownerId).collectAsState(initial = emptyList()).value

            TxnScreen(
                ownerName = owner?.let { "${it.lastName} ${it.firstName}" } ?: "Proprietario",
                ownerHorses = horses.map { it.name },
                txns = txns,
                onAddTxn = { dateMs, op, inc, exp ->
                    vm.upsertTxn(
                        Txn(
                            id = 0L,
                            ownerId = ownerId,
                            dateMillis = dateMs,
                            operation = op,
                            incomeCents = inc,
                            expenseCents = exp
                        )
                    )
                }
            )
        }
    }
}
