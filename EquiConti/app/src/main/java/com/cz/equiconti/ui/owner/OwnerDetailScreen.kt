package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.ArrowBack

/**
 * Dettaglio proprietario (versione minima che compila).
 * Mantiene le firme tipiche usate nel NavGraph.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onOpenTxns: (horseId: Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettaglio proprietario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Owner ID: $ownerId")
            // Qui puoi aggiungere la UI reale (cavalli, movimenti, ecc.)
            // Chiama onOpenTxns(horseId) quando vuoi aprire i movimenti di un cavallo.
        }
    }
}
