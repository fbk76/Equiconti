package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.*

object Routes {
    const val OWNERS = "owners"
    const val ADD_OWNER = "addOwner"
    const val OWNER_DETAIL = "ownerDetail/{ownerId}"
}

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.OWNERS) {

        composable(Routes.OWNERS) {
            val vm: OwnersViewModel = hiltViewModel()
            OwnersScreen(
                owners = vm.owners.value,
                onOwnerClick = { owner -> nav.navigate("ownerDetail/${owner.id}") },
                onAddOwner = { nav.navigate(Routes.ADD_OWNER) }
            )
        }

        composable(Routes.ADD_OWNER) {
            val vm: OwnersViewModel = hiltViewModel()
            AddOwnerScreen(
                nav = nav,
                onSave = { owner -> vm.saveOwner(owner) }
            )
        }

        composable(
            route = Routes.OWNER_DETAIL,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) {
            OwnerDetailScreen(
                onBack = { nav.popBackStack() },
                onAddHorse = { /* nav verso schermata cavallo, se/quando pronta */ }
            )
        }
    }
}
