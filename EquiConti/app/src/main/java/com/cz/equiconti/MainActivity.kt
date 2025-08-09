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

/**
 * Activity d’ingresso con protezione: prova a caricare la UI reale.
 * Se qualcosa lancia eccezione, mostra una schermata di fallback e logga l’errore.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // stato che decide se mostrare la UI reale o il fallback
            val showFallback = remember { mutableStateOf(false) }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (!showFallback.value) {
                    try {
                        // 🔵 QUI viene caricata la tua UI reale
                        MainApp()
                    } catch (t: Throwable) {
                        // Logga e passa al fallback
                        Log.e("EquiConti", "Errore in composizione UI", t)
                        Toast
                            .makeText(this, "Errore di avvio: caricata schermata di emergenza", Toast.LENGTH_LONG)
                            .show()
                        showFallback.value = true
                    }
                } else {
                    FallbackScreen()
                }
            }
        }
    }
}

/**
 * 🔁 Fallback molto semplice mostrato se la UI reale fallisce.
 */
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
 * ✅ UI REALE (sostituisci il contenuto con la tua app).
 *
 * Per rimettere la tua interfaccia:
 * - incolla qui il tuo NavHost / schermate / composables principali
 *   (ad esempio OwnersScreen/OwnerDetail/TxnScreen, ecc.).
 */
@Composable
fun MainApp() {
    // Implementazione “safe” di default per compilare subito:
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
