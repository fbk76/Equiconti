package com.cz.equiconti.ui.nav
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "owners") {
        composable("owners") { OwnersScreen(onOpenOwner = { id -> navController.navigate("owner/$id") }) }
        composable("owner/{ownerId}", arguments = listOf(navArgument("ownerId") { type = NavType.LongType })) {
            OwnerDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
