package com.cz.equiconti

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// ====== IMPORTA LE TUE SCREEN REALI ======
import com.cz.equiconti.ui.OwnersScreen
import com.cz.equiconti.ui.OwnerDetailScreen
import com.cz.equiconti.ui.ReportScreen
// Se hai altre schermate, importa e aggiungi le route come sotto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val showFallback = remember { mutableStateOf(false) }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (!showFallback.value) {
                    try {
                        EquiAppNav()
                    } catch (t: Throwable) {
                        Log.e("EquiConti", "Errore in composizione UI", t)
                        Toast.makeText(
                            this,
                            "Errore di avvio: caricata schermata di emergenza",
                            Toast.LENGTH_LONG
                        ).show()
                        showFallback.value = true
                    }
                } else {
                    FallbackScreen()
                }
            }
        }
    }
}

@Composable
private fun EquiAppNav() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "owners") {

        // Lista proprietari
        composable("owners") {
            OwnersScreen(nav = nav, vm = hiltViewModel())
        }

        // Dettaglio proprietario
        composable(
            route = "owner/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            OwnerDetailScreen(nav = nav, ownerId = ownerId, vm = hiltViewModel())
        }

        // Report: SOLO ownerId (niente from/to)
        composable(
            route = "report/{ownerId}",
            arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
            ReportScreen(nav = nav, ownerId = ownerId, vm = hiltViewModel())
        }

        // Se hai altre schermate, aggiungile qui
        // es: composable("txn") { TxnScreen(nav = nav, vm = hiltViewModel()) }
    }
}

@Composable
private fun FallbackScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Errore di avvio.\nProva a riaprire l’app o invia i log.",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
