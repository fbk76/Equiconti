package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "owners") {
        composable("owners") { OwnersScreen(nav) }
        composable("owner/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
            OwnerDetailScreen(nav, id)
        }
        composable("txns/{ownerId}") { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getString("ownerId")?.toLongOrNull() ?: 0L
            TxnScreen(nav, ownerId)
        }
        composable("report/{ownerId}") { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getString("ownerId")?.toLongOrNull() ?: 0L
            ReportScreen(nav, ownerId)
        }
    }
}
