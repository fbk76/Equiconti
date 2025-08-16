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

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val nav = rememberNavController()
    val vm: OwnersViewModel = hiltViewModel()

    NavHost(
        navController = nav,
        startDestination = "owners",
        modifier = modifier
    ) {
        // lista proprietari
        composable("owners") {
            val owners = vm.owners.collectAsState().value
            OwnersListScreen(
                owners = owners,
                onAddOwner = { nav.navigate("owner/new") },
                onOpenOwner = { owner -> nav.navigate("owner/${owner.id}") }
            )
        }

        // dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { back ->
            val ownerId = back.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onAddHorse = { nav.navigate("owner/$ownerId/addHorse") },
                onOpenTxnForHorse = { horseId -> nav.navigate("horse/$horseId/txns") }
            )
        }

        // aggiungi cavallo
        composable(
            route = "owner/{ownerId}/addHorse",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { back ->
            val ownerId = back.arguments?.getLong("ownerId") ?: 0L
            HorseEditScreen(
                ownerId = ownerId,
                onCancel = { nav.popBackStack() },
                onSaved = { nav.popBackStack() }
            )
        }

        // movimenti per cavallo
        composable(
            route = "horse/{horseId}/txns",
            arguments = listOf(navArgument("horseId") { type = NavType.LongType })
        ) { back ->
            val horseId = back.arguments?.getLong("horseId") ?: 0L
            TxnScreen(
                horseId = horseId,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
