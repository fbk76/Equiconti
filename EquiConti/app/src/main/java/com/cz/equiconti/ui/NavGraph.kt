package com.cz.equiconti.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.owner.AddOwnerScreen
import com.cz.equiconti.ui.owner.HorsesScreen
import com.cz.equiconti.ui.owner.OwnerDetailScreen
import com.cz.equiconti.ui.owner.OwnersScreen
import com.cz.equiconti.ui.owner.OwnersViewModel
import com.cz.equiconti.ui.txn.TxnScreen

object Routes {
    const val OWNERS = "owners"
    const val OWNER_ADD = "owner/add"
    const val OWNER_DETAIL = "owner/{ownerId}"
    const val OWNER_HORSES = "owner/{ownerId}/horses"
    const val OWNER_TXNS = "owner/{ownerId}/txns"
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = Routes.OWNERS,
) {
    val navController = rememberNavController()
    val vm: OwnersViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Lista proprietari
        composable(Routes.OWNERS) {
            OwnersScreen(
                onOwnerClick
