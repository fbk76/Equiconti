package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner

@Composable
fun OwnersScreen(
    onOpenOwner: (Owner) -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owners by vm.owners.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Proprietari") }) }
    ) { pad ->
        if (owners.isEmpty()) {
            Box(Modifier.padding(pad).padding(16.dp)) {
                Text("Nessun proprietario")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(owners) { owner ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenOwner(owner) }
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            // 🔁 Sostituito firstName/lastName -> name
                            Text(owner.name, style = MaterialTheme.typography.titleMedium)
                            owner.phone?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                        }
                    }
                }
            }
        }
    }
}
