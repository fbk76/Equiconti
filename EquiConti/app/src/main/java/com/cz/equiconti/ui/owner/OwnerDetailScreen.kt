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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Dettaglio proprietario con lista cavalli.
 * Mostra: titolo, bottone "Movimenti", FAB "Aggiungi cavallo".
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
    // NB: vm.ownerId è quello della route: lo usiamo ma manteniamo la firma richiesta
    val owner by vm.owner
    val horses by vm.horses

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(owner?.name ?: "Proprietario #$ownerId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    TextButton(onClick = onOpenTxns) {
                        Text("Movimenti")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHorse) {
                Icon(Icons.Filled.Add, contentDescription = "Nuovo cavallo")
            }
        }
    ) { pad ->
        if (horses.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessun cavallo. Tocca + per aggiungerne uno.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(horses, key = { it.id }) { h ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(h.name, style = MaterialTheme.typography.titleMedium)
                            if (!h.notes.isNullOrBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Text(h.notes!!, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
