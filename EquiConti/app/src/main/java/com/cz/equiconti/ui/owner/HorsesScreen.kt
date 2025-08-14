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
import com.cz.equiconti.data.Horse
import kotlin.math.roundToLong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorsesScreen(
    ownerName: String,
    horses: List<Horse>,
    onBack: () -> Unit,
    onAddHorse: (name: String, amountCents: Long) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val totalCents = remember(horses) { horses.sumOf { it.monthlyFeeCents } }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Cavalli • $ownerName") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi cavallo")
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            if (horses.isEmpty()) {
                Text("Nessun cavallo", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(horses, key = { it.id }) { h ->
                        Surface(tonalElevation = 1.dp) {
                            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(h.name, style = MaterialTheme.typography.titleMedium)
                                Text(formatEuro(h.monthlyFeeCents))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Divider()
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Totale importi", style = MaterialTheme.typography.titleMedium)
                    Text(formatEuro(totalCents), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }

    if (showDialog) AddHorseDialog(
        onDismiss = { showDialog = false },
        onSave = { name, euro ->
            val cents = (euro * 100).roundToLong()
            onAddHorse(name, cents)
            showDialog = false
        }
    )
}

@Composable
private fun AddHorseDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, amountEuro: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onSave(name.trim(), amount.toDoubleOrNull() ?: 0.0) },
                enabled = name.isNotBlank()
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
        title = { Text("Nuovo cavallo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Nome cavallo") })
                OutlinedTextField(amount, { amount = it }, label = { Text("Importo (€)") })
            }
        }
    )
}

private fun formatEuro(cents: Long): String {
    val s = cents / 100.0
    return "€ " + String.format("%.2f", s)
}
