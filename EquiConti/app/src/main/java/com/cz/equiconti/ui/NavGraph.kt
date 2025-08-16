package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen

/**
 * Semplice grafo di navigazione:
 * - owners               -> lista proprietari
 * - owner/{ownerId}      -> dettaglio proprietario
 *
 * Nota: per ora navighiamo a:
 *   - "horse/edit/{ownerId}" quando si preme "Aggiungi cavallo"
 *   - "txns/{ownerId}"     quando si premono i movimenti (puoi cambiare route in seguito)
 */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {
        // Lista proprietari
        composable("owners") {
            OwnersScreen(
                onOpenOwner = { ownerId ->
                    navController.navigate("owner/$ownerId")
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
            val ownerIdArg = backStackEntry.arguments?.getLong("ownerId") ?: 0L

            OwnerDetailScreen(
                ownerId = ownerIdArg,
                onBack = { navController.popBackStack() },
                onAddHorse = {
                    // rotta di esempio per la schermata di edit/creazione cavallo
                    navController.navigate("horse/edit/$ownerIdArg")
                },
                onOpenTxns = {
                    // rotta di esempio per i movimenti; adatta a "txns/{horseId}" se la tua schermata usa l'id del cavallo
                    navController.navigate("txns/$ownerIdArg")
                }
            )
        }

        // --- ESEMPI (facoltativi): aggiungi qui le altre schermate quando le colleghi ---
        // composable("horse/edit/{ownerId}") { ... }
        // composable("txns/{horseOrOwnerId}") { ... }
        // composable("report/{ownerId}") { ... }
    }
}
