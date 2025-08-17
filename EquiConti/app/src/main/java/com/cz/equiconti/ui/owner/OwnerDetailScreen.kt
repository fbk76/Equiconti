package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Dettaglio Proprietario:
 * - carica dati e cavalli via ViewModel
 * - FAB per aggiungere cavallo
 * - azione "Movimenti" nell'appbar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenTxns: () -> Unit,
    vm: OwnerDetailViewModel = hiltViewModel()
) {
    // garantiamo che il vm sia quello dell'owner corrente (da SavedStateHandle)
    val ownerState = vm.owner.collectAsState()
    val horsesState = vm.horses.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ownerState.value?.name ?: "Proprietario #$ownerId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    TextButton(onClick = onOpenTxns) { Text("Movimenti") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHorse) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi cavallo")
            }
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Cavalli del proprietario", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            val horses = horsesState.value
            if (horses.isEmpty()) {
                Text("Nessun cavallo. Tocca + per aggiungerne uno.")
            } else {
                LazyColumn {
                    items(horses) { h ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* futuro dettaglio cavallo */ }
                                .padding(vertical = 10.dp)
                        ) {
                            Text(h.name, style = MaterialTheme.typography.bodyLarge)
                            if ((h.notes ?: "").isNotBlank()) {
                                Spacer(Modifier.height(2.dp))
                                Text(h.notes!!, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
