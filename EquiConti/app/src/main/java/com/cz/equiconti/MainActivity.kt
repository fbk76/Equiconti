package com.cz.equiconti

import android.os.Bundle
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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Se in futuro vuoi mostrare il fallback, potrai settare questo stato da eventi leciti.
            val showFallback = remember { mutableStateOf(false) }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (showFallback.value) {
                    FallbackScreen()
                } else {
                    MainApp()
                }
            }
        }
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

/**
 * Qui va la tua UI reale (NavHost / schermate principali).
 */
@Composable
fun MainApp() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "EquiConti è avviata!",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
