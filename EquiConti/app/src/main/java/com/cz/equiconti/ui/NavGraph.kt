package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel

@Composable
fun AppNavGraph(
    navController: NavController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {
        // Lista proprietari
        composable("owners") {
            val vm: OwnersViewModel = hiltViewModel()
            OwnersScreen(
                nav = navController,
                state = vm.state,
                onAddOwner = { navController.navigate("addOwner") },
                onOwnerClick = { ownerId ->
                    // se/quando avrai il dettaglio: navController.navigate("owner/$ownerId")
                }
            )
        }

        // Aggiungi proprietario
        composable("addOwner") {
            val vm: OwnersViewModel = hiltViewModel()
            AddOwnerScreen(
                nav = navController,
                onSave = { owner -> vm.save(owner) }
            )
        }
    }
}
