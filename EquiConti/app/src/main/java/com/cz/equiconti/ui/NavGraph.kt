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
import com.cz.equiconti.ui.owner.AddHorseScreen
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel
import com.cz.equiconti.ui.txn.TxnScreen
import java.time.ZoneId
import kotlin.math.roundToLong

@Composable
fun NavGraph(navController: NavHostController) {

    val vm: OwnersViewModel = hiltViewModel()
    val owners by vm.owners.collectAsState()

    NavHost(navController = navController, startDestination = "owners") {

        // LISTA PROPRIETARI
composable("owners") {
    val vm: OwnersViewModel = hiltViewModel()
    val owners by vm.owners.collectAsState()

    OwnersScreen(
        owners = owners,
        onAddOwner = { lastName, firstName ->
            // crea e salva subito il proprietario
            vm.upsertOwner(
                com.cz.equiconti.data.Owner(
                    ownerId = 0L,          // usa 0/autoId
                    lastName = lastName.trim(),
                    firstName = firstName.trim()
                )
            )
        },
        onOpenOwner = { id ->
            navController.navigate("owner/$id")
        }
    )
}

        // NUOVO PROPRIETARIO (lo screen salva da solo tramite VM)
        composable("owner/add") {
            AddOwnerScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // DETTAGLIO PROPRIETARIO
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onAddHorse = { navController.navigate("owner/$ownerId/addHorse") },
                onOpenTxns = { navController.navigate("owner/$ownerId/txns") }
            )
        }

        // NUOVO CAVALLO
        composable(
            route = "owner/{ownerId}/addHorse",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            AddHorseScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onSave = { horse ->
                    // lo screen ci passa direttamente l'entity corretta
                    vm.upsertHorse(horse)
                    navController.popBackStack()
                }
            )
        }

        // MOVIMENTI
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L

            val owner by vm.ownerFlow(ownerId).collectAsState(initial = null)
            val horses by vm.horses(ownerId).collectAsState(initial = emptyList())
            val txns by vm.txns(ownerId).collectAsState(initial = emptyList())

            TxnScreen(
                ownerName = owner?.let { "${it.lastName} ${it.firstName}" } ?: "Proprietario",
                ownerHorses = horses.map { it.name },
                txns = txns,
                onAddTxn = { dateMillis, op, inc, exp ->
                    vm.upsertTxn(
                        com.cz.equiconti.data.Txn(
                            txnId = 0L,            // usa il nome del PK del tuo data class
                            ownerId = ownerId,
                            dateMillis = dateMillis,
                            operation = op.trim(),
                            incomeCents = inc,
                            expenseCents = exp
                        )
                    )
                }
            )
        }
    }
}
