package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnersScreen(
    owners: List<Owner>,
    onAddOwner: (lastName: String, firstName: String) -> Unit,
    onOpenOwner: (ownerId: Long) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Proprietari") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi")
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            val sorted = remember(owners) {
                owners.sortedWith(compareBy({ it.lastName.lowercase() }, { it.firstName.lowercase() }))
            }
            if (sorted.isEmpty()) {
                Text("Nessun proprietario", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sorted, key = { it.id }) { o ->
                        Surface(
                            tonalElevation = 1.dp,
                            shadowElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenOwner(o.id) }
                                    .padding(12.dp)
                            ) {
                                Text("${o.lastName} ${o.firstName}", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) AddOwnerDialog(
        onDismiss = { showDialog = false },
        onSave = { ln, fn -> onAddOwner(ln, fn); showDialog = false }
    )
}

@Composable
private fun AddOwnerDialog(
    onDismiss: () -> Unit,
    onSave: (lastName: String, firstName: String) -> Unit
) {
    var ln by remember { mutableStateOf("") }
    var fn by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onSave(ln.trim(), fn.trim()) }, enabled = ln.isNotBlank() && fn.isNotBlank()) {
                Text("Salva")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
        title = { Text("Nuovo proprietario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = ln, onValueChange = { ln = it }, label = { Text("Cognome") })
                OutlinedTextField(value = fn, onValueChange = { fn = it }, label = { Text("Nome") })
            }
        }
    )
}
