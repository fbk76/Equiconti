package com.cz.equiconti.ui
import com.cz.equiconti.data.Txn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    // carica i movimenti del proprietario
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
                        headlineContent = { Text(formatDate(t.dateMillis) + " • " + t.operation) },
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
        TxnDialog(
            ownerId = ownerId,
            onDismiss = { showAdd = false },
            onSave = { newTxn ->
                vm.addTxn(newTxn)
                showAdd = false
            }
        )
    }
}

/** Dialog per aggiungere un movimento. */
@Composable
private fun TxnDialog(
    ownerId: Long,
    onDismiss: () -> Unit,
    onSave: (Txn) -> Unit
) {
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) } // yyyy-MM-dd
    var operation by remember { mutableStateOf("") }
    var inc by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val dateMillis = parseDateToMillis(dateText)
                    val income = ((inc.toDoubleOrNull() ?: 0.0) * 100).toLong()
                    val expense = ((exp.toDoubleOrNull() ?: 0.0) * 100).toLong()

                    onSave(
                        Txn(
                            ownerId = ownerId,
                            horseId = null,                 // opzionale
                            dateMillis = dateMillis,        // <-- CAMPO CORRETTO
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
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Data (yyyy-MM-dd)") }
                )
                OutlinedTextField(
                    value = operation,
                    onValueChange = { operation = it },
                    label = { Text("Operazione") }
                )
                OutlinedTextField(
                    value = inc,
                    onValueChange = { inc = it },
                    label = { Text("Entrate (€)") }
                )
                OutlinedTextField(
                    value = exp,
                    onValueChange = { exp = it },
                    label = { Text("Uscite (€)") }
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (facoltative)") }
                )
            }
        }
    )
}

/* ----------------- Utilità semplici ----------------- */

private fun parseDateToMillis(text: String): Long {
    return try {
        val d = LocalDate.parse(text) // formato yyyy-MM-dd
        d.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } catch (_: Exception) {
        // fallback: oggi
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}

private fun formatDate(millis: Long): String {
    val d = java.time.Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault()).toLocalDate()
    return d.toString() // yyyy-MM-dd
}
