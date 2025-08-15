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
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Txn
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

    NavHost(navController = navController, startDestination = "owners") {

        composable("owners") {
            OwnersScreen(nav = navController)
        }

        // ── Nuovo proprietario
        composable("owner/add") {
            AddOwnerScreen(
                onBack = { navController.popBackStack() },
                onSave = { lastName: String, firstName: String ->
                    vm.upsertOwner(
                        Owner(
                            id = 0L,
                            lastName = lastName.trim(),
                            firstName = firstName.trim()
                        )
                    )
                    navController.popBackStack()
                }
            )
        }

        // ── Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onAddHorse = { navController.navigate("owner/$ownerId/addHorse") },
                onOpenTxns  = { navController.navigate("owner/$ownerId/txns") }
            )
        }

        // ── Aggiungi cavallo
        composable(
            route = "owner/{ownerId}/addHorse",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            AddHorseScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onSave = { name: String, monthlyFeeEuro: Double ->
                    vm.upsertHorse(
                        Horse(
                            id = 0L,
                            ownerId = ownerId,
                            name = name.trim(),
                            monthlyFeeCents = (monthlyFeeEuro * 100).roundToLong()
                        )
                    )
                    navController.popBackStack()
                }
            )
        }

        // ── Movimenti
        composable(
            route = "owner/{ownerId}/txns",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L

            // Dati osservati
            val owner by vm.ownerFlow(ownerId).collectAsState(initial = null)
            val horses by vm.horses(ownerId).collectAsState(initial = emptyList())
            val txns by vm.txns(ownerId).collectAsState(initial = emptyList())

            TxnScreen(
                ownerName = owner?.let { "${it.lastName} ${it.firstName}" } ?: "Proprietario",
                ownerHorses = horses.map { it.name },
                txns = txns,
                onAddTxn = { dateMillis: Long, op: String, inc: Long, exp: Long ->
                    vm.upsertTxn(
                        Txn(
                            id = 0L,
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
