package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * Dettaglio proprietario.
 *
 * - Il FAB/azione “+” nella TopAppBar porta a: owner/{ownerId}/addHorse
 * - Non espone più il parametro onAddHorse: la navigazione è gestita internamente
 */
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onOpenTxns: () -> Unit,
    nav: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettaglio proprietario #$ownerId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.automirrored.filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                },
                actions = {
                    // Aggiungi cavallo
                    IconButton(onClick = { nav.navigate("owner/$ownerId/addHorse") }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.filled.Add,
                            contentDescription = "Aggiungi cavallo"
                        )
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
        ) {
            // Qui puoi mostrare i dati del proprietario, lista cavalli, ecc.
            Text("Scheda proprietario in sviluppo")
            // Puoi aggiungere un bottone/azione per movimenti:
            // Button(onClick = onOpenTxns) { Text("Vedi movimenti") }
        }
    }
}
