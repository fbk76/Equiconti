package com.cz.equiconti.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TxnScreen(nav: NavController, ownerId: Long, vm: TxnVm = hiltViewModel()) {
    LaunchedEffect(ownerId) { vm.load(ownerId) }
    val txns by vm.txns
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar("Movimenti") { nav.popBackStack() } },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) { Text("+") }
        }
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
                                "Entrate: ${formatCurrency(t.incomeCents)}  " +
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
            onSave = {
                vm.addTxn(it)
                showAdd = false
            }
        )
    }
}

@Composable
private fun TxnDialog(
    onDismiss: () -> Unit,
    onSave: (Txn) -> Unit,
    ownerId: Long
) {
    var dateStr by remember { mutableStateOf(LocalDate.now().toString()) } // yyyy-MM-dd
    var operation by remember { mutableStateOf("") }
    var inc by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("") }
    var horseIdStr by remember { mutableStateOf("") } // opzionale
    var note by remember { mutableStateOf("") }       // opzionale

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val income = ((inc.toDoubleOrNull() ?: 0.0) * 100).toLong()
                    val expense = ((exp.toDoubleOrNull() ?: 0.0) * 100).toLong()
                    val dateMillis = parseDateToMillis(dateStr) ?: System.currentTimeMillis()
                    val horseId = horseIdStr.toLongOrNull()

                    onSave(
                        Txn(
                            ownerId = ownerId,
                            horseId = horseId,
                            dateMillis = dateMillis,
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
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = dateStr,
                    onValueChange = { dateStr = it },
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
                    value = horseIdStr,
                    onValueChange = { horseIdStr = it },
                    label = { Text("HorseId (opzionale)") }
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Nota (opzionale)") }
                )
            }
        }
    )
}

/* ===== util locali per la schermata ===== */

private fun parseDateToMillis(yyyyMmDd: String): Long? = try {
    val ld = LocalDate.parse(yyyyMmDd)
    ld.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
} catch (_: Throwable) {
    null
}

private fun formatDate(millis: Long): String {
    val ld = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    return ld.toString() // yyyy-MM-dd
}
