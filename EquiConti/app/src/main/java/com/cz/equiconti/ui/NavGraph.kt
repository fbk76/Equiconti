package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavController
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen

object Routes {
    const val OWNERS = "owners"
    const val OWNER_ADD = "owner/add"
    const val OWNER_DETAIL = "owner/{id}"
}

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.OWNERS) {

        composable(Routes.OWNERS) {
            OwnersScreen(
                nav = nav,
                onOwnerClick = { ownerId -> nav.navigate("owner/$ownerId") },
                onAddOwner = { nav.navigate(Routes.OWNER_ADD) }
            )
        }

        composable(Routes.OWNER_ADD) {
            AddOwnerScreen(
                nav = nav,
                ownerId = null,
                onSaved = { savedId -> nav.navigate("owner/$savedId") { popUpTo(Routes.OWNERS) { inclusive = false } } }
            )
        }

        composable(
            route = Routes.OWNER_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("id") ?: 0L
            OwnerDetailScreen(
                nav = nav,
                ownerId = id,
                onEdit = { nav.navigate(Routes.OWNER_ADD) }
            )
        }
    }
}
