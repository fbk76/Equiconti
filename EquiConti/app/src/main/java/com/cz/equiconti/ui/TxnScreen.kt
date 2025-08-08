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

@Composable
fun TxnScreen(nav: NavController, ownerId: Long, vm: TxnVm = hiltViewModel()) {
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
                        headlineContent = { Text("${t.date} • ${t.operation}") },
                        supportingContent = { Text("Entrate: " + formatCurrency(t.incomeCents) + "  Uscite: " + formatCurrency(t.expenseCents)) },
                        trailingContent = { Text(formatCurrency(running)) }
                    )
                    Divider()
                }
            }
        }
    }

    if (showAdd) {
        TxnDialog(onDismiss = { showAdd = false }, onSave = { vm.addTxn(it); showAdd = false }, ownerId = ownerId)
    }
}

@Composable
fun TxnDialog(onDismiss: () -> Unit, onSave: (Txn) -> Unit, ownerId: Long) {
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var operation by remember { mutableStateOf("") }
    var inc by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val income = (inc.toDoubleOrNull() ?: 0.0) * 100
                    val expense = (exp.toDoubleOrNull() ?: 0.0) * 100
                    onSave(Txn(ownerId = ownerId, date = date, operation = operation, incomeCents = income.toLong(), expenseCents = expense.toLong()))
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
            }
        }
    )
}
