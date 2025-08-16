package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Versione semplificata:
 * - Niente ownerFlow / horses (placeholders locali) finché non definisci il ViewModel reale.
 * - Icona compatibile: Icons.Filled.List (al posto di Receipt).
 */
@Composable
fun OwnerDetailScreen(
    onBack: () -> Unit = {}
) {
    // Placeholders temporanei per evitare unresolved reference
    val ownerName: String? = null
    val horses: List<String> = emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ownerName ?: "Dettaglio proprietario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // icona garantita
                        Icon(imageVector = Icons.Filled.List, contentDescription = "Indietro")
                    }
                }
            )
        },
        content = { innerPadding: PaddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(text = "Informazioni proprietario")
                if (horses.isEmpty()) {
                    Text(text = "Nessun cavallo associato")
                } else {
                    horses.forEach { h ->
                        Text("• $h")
                    }
                }
            }
        }
    )
}
