package com.cz.equiconti.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.util.formatCurrency
import com.cz.equiconti.vm.TxnVm
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToLong

@Composable
fun TxnScreen(
    nav: NavController,
    ownerId: Long,
    vm: TxnVm = hiltViewModel()
) {
    LaunchedEffect(ownerId) { vm.load(ownerId) }
    val txns by vm.txns.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Movimenti") }) },
        floatingActionButton = { FloatingActionButton(onClick = { showAdd = true }) { Text("+") } }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            LazyColumn {
                var running = 0L
                items(txns) { t ->
                    running += t.incomeCents - t.expenseCents
                    ListItem(
                        headlineContent = { Text("${formatDate(t.dateMillis)} • ${t.operation}") },
                        supportingContent = {
                            Text(
                                "Entrate: ${formatCurrency(t.incomeCents)}   " +
                                "Uscite: ${formatCurrency(t.expenseCents)}" +
                                (t.note?.let { "   Note: $it" } ?: "")
                            )
                        },
                        trailingContent = { Text(formatCurrency(running)) }
                    )
                    Divider()
                }
            }
        }
    }

    if (showAdd) {
        TxnDialog(
            ownerId = ownerId,
            onDismiss = { showAdd = false },
            onSave = { vm.addTxn(it); showAdd = false }
        )
    }
}

@Composable
private fun TxnDialog(
    ownerId: Long,
    onDismiss: () -> Unit,
    onSave: (com.cz.equiconti.data.Txn) -> Unit
) {
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) } // yyyy-MM-dd
    var operation by remember { mutableStateOf("") }
    var incomeText by remember { mutableStateOf("") }  // euro (es: 12.50)
    var expenseText by remember { mutableStateOf("") } // euro
    var noteText by remember { mutableStateOf("") }
    var horseIdText by remember { mutableStateOf("") } // opzionale

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val dateMillis = LocalDate.parse(dateText)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()

                    val incomeCents = ((incomeText.replace(',', '.').toDoubleOrNull() ?: 0.0) * 100).roundToLong()
                    val expenseCents = ((expenseText.replace(',', '.').toDoubleOrNull() ?: 0.0) * 100).roundToLong()
                    val horseId = horseIdText.toLongOrNull()

                    onSave(
                        com.cz.equiconti.data.Txn(
                            ownerId = ownerId,
                            horseId = horseId,
                            dateMillis = dateMillis,
                            operation = operation.trim(),
                            incomeCents = incomeCents,
                            expenseCents = expenseCents,
                            note = noteText.ifBlank { null }
                        )
                    )
                },
                enabled = operation.isNotBlank() && dateText.isNotBlank()
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
        title = { Text("Nuovo movimento") },
        text = {
            Column(Modifier.padding(top = 4.dp)) {
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Data (yyyy-MM-dd)") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = operation,
                    onValueChange = { operation = it },
                    label = { Text("Operazione") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = incomeText,
                    onValueChange = { incomeText = it },
                    label = { Text("Entrate (€)") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = expenseText,
                    onValueChange = { expenseText = it },
                    label = { Text("Uscite (€)") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = horseIdText,
                    onValueChange = { horseIdText = it },
                    label = { Text("ID cavallo (opzionale)") },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Note (opzionale)") }
                )
            }
        }
    )
}

private fun formatDate(millis: Long): String {
    val dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
    return dt.format(DateTimeFormatter.ISO_DATE) // yyyy-MM-dd
}
