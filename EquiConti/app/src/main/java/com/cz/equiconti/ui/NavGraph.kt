package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddHorseScreen
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.HorsesScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel
import com.cz.equiconti.ui.txn.TxnScreen

private object Routes {
    const val OWNERS = "owners"
    const val OWNER_ADD = "owner/add"
    const val OWNER_DETAIL = "owner/{ownerId}"
    const val OWNER_HORSES = "owner/{ownerId}/horses"
    const val HORSE_ADD = "owner/{ownerId}/horse/add"
    const val OWNER_TXN = "owner/{ownerId}/txn"
    const val ARG_ID = "ownerId"
}

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    val vm: OwnersViewModel = hiltViewModel()

    NavHost(navController = nav, startDestination = Routes.OWNERS) {

        // ── Lista proprietari
        composable(Routes.OWNERS) {
            OwnersScreen(
                navController = nav,
                vm = vm
            )
        }

        // ── Aggiungi proprietario
        composable(Routes.OWNER_ADD) {
            AddOwnerScreen(
                onSave = { owner ->
                    vm.upsertOwner(owner)
                    nav.popBackStack()
                },
                onBack = { nav.popBackStack() }
            )
        }

        // ── Dettaglio proprietario
        composable(
            route = Routes.OWNER_DETAIL,
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong(Routes.ARG_ID)
            OwnerDetailScreen(
                ownerId = ownerId,
                nav = nav
            )
        }

        // ── Lista cavalli del proprietario
        composable(
            route = Routes.OWNER_HORSES,
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong(Routes.ARG_ID)
            HorsesScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onAddHorse = { id -> nav.navigate("owner/$id/horse/add") }
            )
        }

        // ── Aggiungi cavallo
        composable(
            route = Routes.HORSE_ADD,
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong(Routes.ARG_ID)
            AddHorseScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onSaved = { nav.popBackStack() }
            )
        }

        // ── Movimenti (entrate/uscite) del proprietario
        composable(
            route = Routes.OWNER_TXN,
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong(Routes.ARG_ID)
            TxnScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
