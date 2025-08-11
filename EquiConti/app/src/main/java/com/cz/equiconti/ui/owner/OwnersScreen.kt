package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun OwnersScreen(
    nav: NavHostController
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate("addOwner") }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder lista vuota
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Proprietari", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                Text(
                    "Nessun proprietario (placeholder).\nTocca + per aggiungere.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(24.dp))
                // voce finta per test navigazione dettaglio
                Text(
                    "Apri dettaglio finto (id=1)",
                    modifier = Modifier.clickable { nav.navigate("owner/1") }
                )
            }
        }
    }
}
