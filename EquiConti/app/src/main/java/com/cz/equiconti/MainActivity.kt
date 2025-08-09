package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.OwnersScreen
import com.cz.equiconti.ui.OwnerDetailScreen
import com.cz.equiconti.ui.TxnScreen
import com.cz.equiconti.ui.ReportScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainApp()
            }
        }
    }
}

/** Rotte dell’app */
private object Routes {
    const val OWNERS = "owners"
    const val OWNER_DETAIL = "owner/{ownerId}"
    const val TXNS = "owner/{ownerId}/txns"
    const val REPORT = "owner/{ownerId}/report?from={from}&to={to}"
}

/** UI reale con Navigation Compose */
@androidx.compose.runtime.Composable
fun MainApp() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.OWNERS) {

        // Lista proprietari
        composable(Routes.OWNERS) {
            OwnersScreen(
                onOpenOwner = { ownerId ->
                    nav.navigate("owner/$ownerId")
                }
            )
        }

        // Dettaglio proprietario + cavalli
        composable(
            route = Routes.OWNER_DETAIL,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments!!.getLong("ownerId")
            OwnerDetailScreen(
                ownerId = ownerId,
                onOpenTxns = { nav.navigate("owner/$ownerId/txns") },
                onOpenReport = { from, to ->
                    // from/to in formato yyyy-MM-dd
                    nav.navigate("owner/$ownerId/report?from=$from&to=$to")
                },
                onBack = { nav.popBackStack() }
            )
        }

        // Movimenti
        composable(
            route = Routes.TXNS,
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments!!.getLong("ownerId")
            TxnScreen(
                ownerId = ownerId,
                onBack = { nav.popBackStack() }
            )
        }

        // Report
        composable(
            route = Routes.REPORT,
            arguments = listOf(
                navArgument("ownerId") { type = NavType.LongType },
                navArgument("from") { type = NavType.StringType; defaultValue = "" },
                navArgument("to") { type = NavType.StringType; defaultValue = "" },
            )
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments!!.getLong("ownerId")
            val from = backStackEntry.arguments!!.getString("from").orEmpty()
            val to = backStackEntry.arguments!!.getString("to").orEmpty()
            ReportScreen(
                ownerId = ownerId,
                from = from,
                to = to,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
