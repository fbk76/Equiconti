package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenTxns: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Proprietario #$ownerId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Qui in seguito potrai caricare i dati reali e la lista cavalli
            Text("Dettagli proprietario (placeholder)")
            Spacer(Modifier.height(16.dp))

            // Azioni principali ben visibili
            Button(
                onClick = onAddHorse,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Aggiungi cavallo") }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onOpenTxns,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Vedi movimenti") }

            Spacer(Modifier.height(24.dp))
            Text("Lista cavalli (placeholder)")
        }
    }
}
