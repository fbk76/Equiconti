package com.cz.equiconti.ui.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

@Composable
fun AppNavGraph(
    navController: androidx.navigation.NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "owners") {

        composable("owners") {
            val vm: OwnersViewModel = hiltViewModel()

            OwnersScreen(
                state = vm.state,                     // se il tuo screen usa uno state; altrimenti passagli la lista
                onOwnerClick = { ownerId ->
                    navController.navigate("owner/$ownerId")
                },
                onAddOwner = {
                    navController.navigate("addOwner")
                }
            )
        }

        composable("addOwner") {
            val vm: OwnersViewModel = hiltViewModel()
            AddOwnerScreen(
                onSave = { owner ->
                    vm.save(owner)
                    navController.popBackStack()      // torna alla lista
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(
                ownerId = ownerId,
                onBack = { navController.popBackStack() },
                onAddHorse = { /* navController.navigate("addHorse/$ownerId") se/quando serve */ }
            )
        }

        // Se hai lo screen movimenti:
        // composable("txn/{ownerId}",
        //     arguments = listOf(navArgument("ownerId"){ type = NavType.LongType })
        // ) { ... }
    }
}
