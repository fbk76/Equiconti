package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenTxns: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    // Carico proprietario e cavalli
    val owner by vm.ownerFlow(ownerId).collectAsState(initial = null)
    val horses by vm.horses(ownerId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(owner?.let { "${it.firstName} ${it.lastName}" } ?: "Proprietario")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("←") }
                },
                actions = {
                    IconButton(onClick = onOpenTxns) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = "Movimenti")
                    }
                    IconButton(onClick = onAddHorse) {
                        Icon(Icons.Default.Add, contentDescription = "Cavallo +")
                    }
                }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("Dati proprietario", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text("Nome: ${owner?.firstName ?: "-"}")
                Text("Cognome: ${owner?.lastName ?: "-"}")
            }
            Spacer(Modifier.height(16.dp))

            Text("Cavalli", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (horses.isEmpty()) {
                Text("Nessun cavallo. Tocca \"Cavallo +\" per aggiungerne uno.")
            } else {
                HorsesList(horses = horses)
            }

            // Totale importi (quota per cavallo)
            val totale = horses.sumOf { it.monthlyFeeCents }
            Spacer(Modifier.height(12.dp))
            Text(
                "Totale Importi: ${formatEuro(totale)}",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun HorsesList(horses: List<Horse>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(horses) { h ->
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(h.name, style = MaterialTheme.typography.titleMedium)
                        Text("Importo: ${formatEuro(h.monthlyFeeCents)}")
                    }
                }
            }
        }
    }
}

private fun formatEuro(cents: Long): String {
    val v = cents / 100.0
    return "€ " + String.format("%.2f", v)
}
