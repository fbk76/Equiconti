package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.ui.OwnersScreen
import com.cz.equiconti.ui.OwnerDetailScreen
import com.cz.equiconti.ui.TxnScreen
import com.cz.equiconti.ui.ReportScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val nav = rememberNavController()

                NavHost(
                    navController = nav,
                    startDestination = "owners"
                ) {
                    // Lista proprietari
                    composable("owners") {
                        // Se OwnersScreen accetta anche il VM, lo fornisco con hiltViewModel()
                        OwnersScreen(nav = nav, vm = hiltViewModel())
                    }

                    // Dettaglio proprietario: ownerId come Long
                    composable(
                        route = "owner/{ownerId}",
                        arguments = listOf(
                            navArgument("ownerId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
                        OwnerDetailScreen(
                            nav = nav,
                            ownerId = ownerId,
                            vm = hiltViewModel()
                        )
                    }

                    // Transazioni per ownerId
                    composable(
                        route = "txns/{ownerId}",
                        arguments = listOf(
                            navArgument("ownerId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
                        TxnScreen(
                            nav = nav,
                            ownerId = ownerId,
                            vm = hiltViewModel()
                        )
                    }

                    // Report: ownerId + from + to (ISO yyyy-MM-dd)
                    composable(
                        route = "report/{ownerId}/{from}/{to}",
                        arguments = listOf(
                            navArgument("ownerId") { type = NavType.LongType },
                            navArgument("from") { type = NavType.StringType },
                            navArgument("to") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
                        val from = backStackEntry.arguments?.getString("from") ?: ""
                        val to = backStackEntry.arguments?.getString("to") ?: ""
                        ReportScreen(
                            nav = nav,
                            ownerId = ownerId,
                            from = from,
                            to = to,
                            vm = hiltViewModel()
                        )
                    }
                }
            }
        }
    }
}
