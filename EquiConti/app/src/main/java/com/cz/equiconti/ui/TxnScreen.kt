package com.cz.equiconti.ui

import androidx.compose.foundation.layout.Column
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
    onSave: (com.cz.equiconti.data.Txn) -> Unit // <-- tipo completamente qualificato
) {
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) } // yyyy-MM-dd
    var operation by remember { mutableStateOf("") }
    var incomeText by remember { mutableStateOf("") }  // euro
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

                    val incomeCents = ((incomeText.toDoubleOrNull() ?: 0.0) * 100).roundToLong()
                    val expenseCents = ((expenseText.toDoubleOrNull() ?: 0.0) * 100).roundToLong()
                    val horseId = horseIdText.toLongOrNull()

                    onSave(
                        com.cz.equiconti.data.Txn( // <-- uso la data class giusta
                            ownerId = ownerId,
                            horseId = horseId,
                            dateMillis = dateMillis,
                            operation = operation,
                            incomeCents = incomeCents,
