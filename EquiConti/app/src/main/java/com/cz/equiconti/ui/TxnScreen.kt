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
import java.time.format.DateTimeFormatter

@Composable
fun TxnScreen(
    nav: NavController,
    ownerId: Long,
    vm: TxnVm = hiltViewModel()
) {
    LaunchedEffect(ownerId) { vm.load(ownerId) }
    val txns by vm.txns.collectAsState(emptyList())
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
                        headlineContent = {
                            Text("${formatDate(t.dateMillis)} • ${t.operation}")
                        },
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
            onSave = { vm.addTxn(it); showAdd = false }
        )
    }
}

private val DATE_FMT: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

private fun formatDate(millis: Long): String =
    LocalDate.ofEpochDay(millis / 86_400_000L).format(DATE_FMT)

/**
 * Dialog per inserire una nuova transazione.
 * Salva usando il nuovo modello Txn (dateMillis in epoch millis).
 */
@Composable
fun TxnDialog(
    onDismiss: () -> Unit,
    onSave: (Txn) -> Unit,
    ownerId: Long
) {
    var dateStr by remember { mutableStateOf(LocalDate.now().format(DATE_FMT)) }
    var operation by remember { mutableStateOf("") }
    var inc by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val income = ((inc.replace(',', '.')).toDoubleOrNull() ?: 0.0) * 100
                    val expense = ((exp.replace(',', '.')).toDoubleOrNull() ?: 0.0) * 100

                    val date = runCatching { LocalDate.parse(dateStr, DATE_FMT) }
                        .getOrElse { LocalDate.now() }
                    val dateMillis = date.atStartOfDay(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()

                    onSave(
                        Txn(
                            txnId = 0,                 // autoGenerate
                            ownerId = ownerId,
                            horseId = null,
                            dateMillis = dateMillis,
                            operation = operation,
                            incomeCents = income.toLong(),
                            expenseCents = expense.toLong(),
                            note = null
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
            }
        }
    )
}
