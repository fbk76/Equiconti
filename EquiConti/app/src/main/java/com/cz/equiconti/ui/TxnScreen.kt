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
import java.time.Instant
import java.time.LocalDate
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

/** Dialog per inserire una nuova transazione */
@Composable
private fun TxnDialog(
    ownerId: Long,
    onDismiss: () -> Unit,
    onSave: (Txn) -> Unit
) {
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) } // yyyy-MM-dd
    var operation by remember { mutableStateOf("") }
    var incomeText by remember { mutableStateOf("") } // euro
    var expenseText by remember { mutableStateOf("") } // euro
    var note by remember { mutableStateOf("") }
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

                    val incomeCents = ((incomeText.toDoubleOrNull() ?: 0.0) * 100).roundToLong()
                    val expenseCents = ((expenseText.toDoubleOrNull() ?: 0.0) * 100).roundToLong()
                    val horseId = horseIdText.toLongOrNull()

                    onSave(
                        Txn
