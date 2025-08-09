package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavHost(nav: NavHostController, start: String = "owners") {
    NavHost(navController = nav, startDestination = start) {

        composable("owners") {
            OwnersScreen(nav = nav)
        }

        // Nuovo: schermata di creazione
        composable("owner/new") {
            AddOwnerScreen(nav = nav)
        }
    }
}
