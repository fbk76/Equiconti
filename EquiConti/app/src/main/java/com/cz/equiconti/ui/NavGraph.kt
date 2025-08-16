package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.horse.HorseEditScreen
import com.cz.equiconti.ui.txn.TxnScreen

private object Routes {
    const val OWNERS = "owners"
    const val OWNER_DETAIL = "owner/{ownerId}"
    const val HORSE_EDIT = "horseEdit/{ownerId}"
    const val TXNS = "txns/{horseId}"
}

@Composable
fun AppNavHost() {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Routes.OWNERS
    ) {
        composable(Routes.OWNERS) {
            OwnersScreen(
                onOwnerClick = { ownerId -> nav.navigate("owner/$ownerId") },
                onAddOwner = { /* se serve */ }
            )
        }

        composable(
            Routes.OWNER_DETAIL,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong("ownerId")
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onAddHorse = { nav.navigate("horseEdit/$ownerId") },
                onOpenTxns = {
                    // se vuoi aprire i movimenti del primo cavallo scegli tu la logica,
                    // qui è solo un esempio navigazionale generico
                    // nav.navigate("txns/$horseId")
                }
            )
        }

        composable(
            Routes.HORSE_EDIT,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStack ->
            val ownerId = backStack.arguments!!.getLong("ownerId")
            HorseEditScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            Routes.TXNS,
            arguments = listOf(navArgument("horseId") { type = NavType.LongType })
        ) { backStack ->
            val horseId = backStack.arguments!!.getLong("horseId")
            TxnScreen(
                horseId = horseId,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
