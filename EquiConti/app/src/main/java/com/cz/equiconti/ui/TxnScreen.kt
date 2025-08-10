// app/src/main/java/com/cz/equiconti/ui/TxnScreen.kt
package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Txn
import com.cz.equiconti.util.formatCurrency
import com.cz.equiconti.vm.TxnVm
import java.time.LocalDate
import java.time.ZoneId

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
        topBar = { TopBar("Movimenti") { nav.popBackStack() } },
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
                                "Uscite: ${formatCurrency(t.expenseCents)}"
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
        AddTxnDialog(
            ownerId = ownerId,
            onDismiss = { showAdd = false },
            onSave = { vm.addTxn(it); showAdd = false }
        )
    }
}

@Composable
private fun AddTxnDialog(
    ownerId: Long,
    onDismiss: () -> Unit,
    onSave: (Txn) -> Unit
) {
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var operation by remember { mutableStateOf("") }
    var inc by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val zone = ZoneId.systemDefault()
                    val millis = LocalDate.parse(date).atStartOfDay(zone).toInstant().toEpochMilli()
                    val income = ((inc.toDoubleOrNull() ?: 0.0) * 100).toLong()
                    val expense = ((exp.toDoubleOrNull() ?: 0.0) * 100).toLong()
                    onSave(
                        Txn(
                            ownerId = ownerId,
                            horseId = null,
                            dateMillis = millis,
                            operation = operation,
                            incomeCents = income,
                            expenseCents = expense,
                            note = note.ifBlank { null }
                        )
                    )
                },
                enabled = operation.isNotBlank()
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
        title = { Text("Nuovo movimento") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(date, { date = it }, label = { Text("Data (yyyy-MM-dd)") })
                OutlinedTextField(operation, { operation = it }, label = { Text("Operazione") })
                OutlinedTextField(inc, { inc = it }, label = { Text("Entrate (€)") })
                OutlinedTextField(exp, { exp = it }, label = { Text("Uscite (€)") })
                OutlinedTextField(note, { note = it }, label = { Text("Note") })
            }
        }
    )
}

private fun formatDate(millis: Long): String {
    val d = java.time.Instant.ofEpochMilli(millis).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
    return d.toString()
}
