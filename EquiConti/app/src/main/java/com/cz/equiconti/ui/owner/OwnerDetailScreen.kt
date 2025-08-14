package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse

@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenTxns: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owner by remember(ownerId) { vm.ownerFlow(ownerId) }.collectAsState(initial = null)
    val horses by remember(ownerId) { vm.horses(ownerId) }.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(owner?.let { "${it.firstName} ${it.lastName}".trim() } ?: "Dettaglio") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.Add, contentDescription = null) } // (usa una icona back se la hai)
                },
                actions = {
                    TextButton(onClick = onOpenTxns) { Text("Movimenti") }
                    TextButton(onClick = onAddHorse) { Text("Cavallo +") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            if (owner == null) {
                Text("Proprietario non trovato.")
                return@Column
            }

            Text("Dati proprietario", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text("Nome: ${owner!!.firstName}")
            Text("Cognome: ${owner!!.lastName}")
            if (!owner!!.phone.isNullOrBlank()) Text("Telefono: ${owner!!.phone}")

            Spacer(Modifier.height(16.dp))
            Text("Cavalli", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (horses.isEmpty()) {
                Text("Nessun cavallo. Tocca \"Cavallo +\" per aggiungerne uno.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(horses, key = { it.id }) { h: Horse ->
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(h.name, style = MaterialTheme.typography.titleMedium)
                                if (h.monthlyFeeCents > 0) {
                                    Text("Quota mensile: € ${"%.2f".format(h.monthlyFeeCents / 100.0)}")
                                }
                                if (!h.notes.isNullOrBlank()) Text("Note: ${h.notes}")
                            }
                        }
                    }
                }
            }
        }
    }
}
