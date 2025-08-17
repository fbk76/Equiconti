package com.cz.equiconti.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen

/**
 * NavGraph principale dell’app.
 * - owners -> lista proprietari
 * - owner/{ownerId} -> dettaglio proprietario
 * - txns/{horseId} -> (stub) lista movimenti cavallo
 */
@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "owners") {

        // Lista proprietari
        composable("owners") {
            OwnersScreen(
                onOwnerClick = { ownerId ->
                    nav.navigate("owner/$ownerId")
                },
                onAddOwner = {
                    // TODO: schermata “nuovo proprietario”
                    // per ora non navighiamo da nessuna parte
                }
            )
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L

            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() },
                onOpenTxns = { horseId ->
                    nav.navigate("txns/$horseId")
                }
            )
        }

        // Stub movimenti cavallo (per evitare rotte mancanti a runtime)
        composable(
            route = "txns/{horseId}",
            arguments = listOf(
                navArgument("horseId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val horseId = backStackEntry.arguments?.getLong("horseId") ?: 0L
            Text("Movimenti cavallo #$horseId (stub)")
        }
    }
}
