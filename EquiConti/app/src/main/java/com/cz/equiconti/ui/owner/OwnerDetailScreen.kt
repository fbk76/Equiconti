package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenTxns: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owner by vm.ownerFlow(ownerId).collectAsState(initial = null)
    val horses by vm.horses(ownerId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(owner?.let { "${it.lastName} ${it.firstName}" } ?: "Proprietario") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                actions = {
                    TextButton(onClick = onOpenTxns) { Text("Movimenti") }
                    TextButton(onClick = onAddHorse) { Text("Cavallo +") }
                }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Dati proprietario", style = MaterialTheme.typography.titleMedium)
            Text("Cognome: ${owner?.lastName ?: "-"}")
            Text("Nome: ${owner?.firstName ?: "-"}")

            Spacer(Modifier.height(16.dp))
            Text("Cavalli", style = MaterialTheme.typography.titleMedium)

            if (horses.isEmpty()) {
                Text("Nessun cavallo. Tocca \"Cavallo +\" per aggiungerne uno.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(horses) { h: Horse ->
                        Text("• ${h.name}  —  quota: € ${"%.2f".format(h.monthlyFeeCents/100.0)}")
                    }
                }
            }
        }
    }
}
