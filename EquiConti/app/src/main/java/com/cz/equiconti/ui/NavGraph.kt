package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "owners"
    ) {
        composable("owners") {
            OwnersScreen(
                onOwnerClick = { ownerId ->
                    navController.navigate("owner/$ownerId")
                },
                onAddClick = {
                    navController.navigate("addOwner")
                }
            )
        }

        composable("addOwner") {
            AddOwnerScreen(
                onSave = { /* dopo salvataggio torna indietro */ navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("owner/{ownerId}") {
            OwnerDetailScreen(
                onBack = { navController.popBackStack
