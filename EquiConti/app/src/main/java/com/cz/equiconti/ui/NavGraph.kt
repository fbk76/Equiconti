package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.HorsesScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel
import com.cz.equiconti.ui.txn.TxnScreen

object Routes {
    const val OWNERS = "owners"
    const val OWNER_ADD = "owner/add"
    const val OWNER_DETAIL = "owner/{ownerId}"
    const val OWNER_HORSES = "owner/{ownerId}/horses"
    const val OWNER_TXNS = "owner/{ownerId}/txns"
}

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    val vm: OwnersViewModel = hiltViewModel()

    NavHost(navController = nav, startDestination = Routes.OWNERS) {

        // Lista proprietari
        composable(Routes.OWNERS) {
            // Versione della tua OwnersScreen che prende navController e vm
            OwnersScreen(
                navController = nav,
                vm = vm
            )
        }

        // Aggiungi proprietario
        composable(Routes.OWNER_ADD) {
            AddOwnerScreen(
                onSave = { owner ->
                    vm.upsertOwner(owner)
                    nav.popBackStack()
                },
                onBack = { nav.popBackStack() }
            )
        }

        // Dettaglio proprietario
        composable(
            route = Routes.OWNER_DETAIL,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong("ownerId")
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onEdit = { /* se vuoi una schermata edit dedicata, portalo a OWNER_ADD o ad una rotta /edit */ nav.navigate(Routes.OWNER_ADD) },
                onOpenHorses = { id -> nav.navigate("owner/$id/horses") },
                onOpenTxns =   { id -> nav.navigate("owner/$id/txns") },
                onDeleted = { nav.popBackStack() }
            )
        }

        // Cavalli del proprietario
        composable(
            route = Routes.OWNER_HORSES,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong("ownerId")
            HorsesScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onAddHorse = { id -> nav.navigate("owner/$id/addHorse") }, // opzionale se creerai la rotta di aggiunta cavallo
                onHorseClick = { /* opzionale: dettaglio cavallo */ }
            )
        }

        // Movimenti del proprietario
        composable(
            route = Routes.OWNER_TXNS,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong("ownerId")
            TxnScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onAddTxn = { /* opzionale: rotta per aggiungere nuovo movimento */ }
            )
        }
    }
}
