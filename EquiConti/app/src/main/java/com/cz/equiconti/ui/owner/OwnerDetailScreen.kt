package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenTxnForHorse: (Long) -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owner: Owner? by vm.ownerFlow(ownerId).collectAsState(initial = null)
    val horses: List<Horse> by vm.horses(ownerId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(owner?.name ?: "Proprietario") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } },
                actions = {
                    IconButton(onClick = onAddHorse) {
                        Icon(Icons.Filled.Add, contentDescription = "Aggiungi cavallo")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {
            Text("Dati proprietario", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(text = "Nome: ${owner?.name ?: "-"}", fontWeight = FontWeight.Medium)

            Spacer(Modifier.height(16.dp))
            Text("Cavalli", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (horses.isEmpty()) {
                Text("Nessun cavallo. Tocca \"+\" per aggiungerne uno.")
            } else {
                HorsesList(horses = horses, onOpenTxnForHorse = onOpenTxnForHorse)
            }

            Spacer(Modifier.height(12.dp))
            Text(text = "Totale cavalli: ${horses.size}", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun HorsesList(
    horses: List<Horse>,
    onOpenTxnForHorse: (Long) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(horses) { h ->
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(h.name, style = MaterialTheme.typography.titleMedium)
                        h.breed?.let { Text("Razza: $it") }
                    }
                    IconButton(onClick = { onOpenTxnForHorse(h.id) }) {
                        Icon(Icons.Filled.ReceiptLong, contentDescription = "Movimenti cavallo")
                    }
                }
            }
        }
    }
}
