package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen

/**
 * NavGraph minimale per far compilare:
 * - rimosse le lambda non presenti (onOwnerClick / onAddOwner / onOpenOwner / onOpenTxns, ecc.)
 * - rotta lista proprietari -> dettaglio proprietario (con ownerId come stringa opzionale)
 *
 * Adegua liberamente in seguito aggiungendo i parametri che realmente esistono nelle tue screen.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "owners"
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable("owners") {
            // Versione senza parametri, in linea con il log (OwnersScreen non li espone).
            OwnersScreen(
                // Se in futuro aggiungi callback, potrai re-introdurli qui.
                onOpenOwner = { ownerId ->
                    navController.navigate("owner/$ownerId")
                }
            )
        }

        // Rotta dettaglio proprietario con parametro (stringa per essere permissivi)
        composable("owner/{ownerId}") {
            // Estrai l'argomento se ti serve; per ora lo ignoriamo nella schermata semplificata
            // val ownerId = it.arguments?.getString("ownerId")
            OwnerDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
