// 2) NavGraph.kt — Navigazione minimale che COMPILA con le nuove firme

package com.cz.equiconti.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cz.equiconti.ui.owner.HorsesScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.AddHorseScreen
import com.cz.equiconti.ui.txn.TxnScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "horses"
    ) {
        composable("horses") {
            HorsesScreen(
                onAddHorse = { navController.navigate("addHorse") },
                content = {
                    // TODO: sostituisci con la tua lista cavalli
                    Text("Lista cavalli")
                }
            )
        }

        composable("addHorse") {
            AddHorseScreen(
                onSave = { name, ageText ->
                    // TODO: salva cavallo (name, ageText.toIntOrNull())
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("ownerDetail") {
            OwnerDetailScreen(
                onSave = { name, phone ->
                    // TODO: salva proprietario
                    navController.popBackStack()
                }
            )
        }

        composable("addOwner") {
            AddOwnerScreen(
                onSave = { name, phone ->
                    // TODO: salva nuovo proprietario
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("txn") {
            TxnScreen(
                onAddTxn = { amount, note ->
                    // TODO: salva movimento
                }
            )
        }
    }
}
