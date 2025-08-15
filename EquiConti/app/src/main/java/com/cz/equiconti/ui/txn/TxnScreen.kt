package com.cz.equiconti.ui.txn

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.cz.equiconti.data.Txn
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.roundToLong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    ownerName: String,
    ownerHorses: List<String>,
    txns: List<Txn>,
    onAddTxn: (dateMillis: Long, operation: String, incomeCents: Long, expenseCents: Long) -> Unit,
) {
    val zone = ZoneId.systemDefault()
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Filtri data
    var from by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var to by remember { mutableStateOf(LocalDate.now()) }

    // Dialog nuovo movimento
    var showAdd by remember { mutableStateOf(false) }

    val filtered = remember(txns, from, to) {
        val fromMs = from.atStartOfDay(zone).toInstant().toEpochMilli()
        val toMs = to.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        txns.filter { it.dateMillis in fromMs..toMs }.sortedBy { it.dateMillis }
    }

    val running = remember(filtered) {
        val accs = mutableListOf<Long>()
        var acc = 0L
        filtered.forEach { t ->
            acc += t.incomeCents - t.expenseCents
            accs.add(acc)
        }
        accs
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti • $ownerName") },
                actions = {
                    IconButton(onClick = { /* TODO: esporta PDF */ }) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Stampa/Esporta")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(Icons.Default.Save, contentDescription = "Aggiungi")
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(12.dp)) {

            if (ownerHorses.isNotEmpty()) {
                Text("Cavalli: " + ownerHorses.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = from.format(fmt),
                    onValueChange = { runCatching { from = LocalDate.parse(it, fmt) } },
                    label = { Text("Da (yyyy-MM-dd)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = to.format(fmt),
                    onValueChange = { runCatching { to = LocalDate.parse(it, fmt) } },
                    label = { Text("A (yyyy-MM-dd)") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Header tabella
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeaderCell("Data", 1.0f)
                HeaderCell("Movimento", 2.2f)
                HeaderCell("Entrate", 1.0f)
                HeaderCell("Uscite", 1.0f)
                HeaderCell("Saldo", 1.0f)
            }
            Divider()

            // Righe (usa itemsIndexed su lista filtrata)
            LazyColumn {
                itemsIndexed(filtered) { idx, t ->
                    val saldo = running[idx]
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BodyCell(formatDate(t.dateMillis, zone), 1.0f)
                        BodyCell(t.operation, 2.2f)
                        BodyCell(formatEuro(t.incomeCents), 1.0f)
                        BodyCell(formatEuro(t.expenseCents), 1.0f)
                        BodyCell(formatEuro(saldo), 1.0f, bold = true)
                    }
                    Divider()
                }
            }

            Spacer(Modifier.height(12.dp))
            val totIn = filtered.sumOf { it.incomeCents }
            val totOut = filtered.sumOf { it.expenseCents }
            val saldoFin = running.lastOrNull() ?: 0L
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Totale Entrate: ${formatEuro(totIn)}")
                Text("Totale Uscite: ${formatEuro(totOut)}")
                Text("Saldo Finale: ${formatEuro(saldoFin)}", fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (showAdd) AddTxnDialog(
        onDismiss = { showAdd = false },
        onSave = { dateStr, descr, incEuro, expEuro ->
            val dateMs = LocalDate.parse(dateStr, fmt).atStartOfDay(zone).toInstant().toEpochMilli()
            val inc = (incEuro * 100).roundToLong()
            val exp = (expEuro * 100).roundToLong()
            onAddTxn(dateMs, descr, inc, exp)
            showAdd = false
        }
    )
}

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(text, modifier = Modifier.weight(weight), fontWeight = FontWeight.SemiBold)
}

@Composable
private fun RowScope.BodyCell(text: String, weight: Float, bold: Boolean = false) {
    Text(
        text,
        modifier = Modifier.weight(weight),
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal
    )
}

@Composable
private fun AddTxnDialog(
    onDismiss: () -> Unit,
    onSave: (date: String, descr: String, incEuro: Double, expEuro: Double) -> Unit
) {
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var date by remember { mutableStateOf(LocalDate.now().format(fmt)) }
    var descr by remember { mutableStateOf("") }
    var inc by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(date, descr.trim(), inc.toDoubleOrNull() ?: 0.0, exp.toDoubleOrNull() ?: 0.0)
                },
                enabled = descr.isNotBlank()
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
        title = { Text("Nuovo movimento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Data (yyyy-MM-dd)") })
                OutlinedTextField(value = descr, onValueChange = { descr = it }, label = { Text("Movimento") })
                OutlinedTextField(value = inc, onValueChange = { inc = it }, label = { Text("Entrate (€)") })
                OutlinedTextField(value = exp, onValueChange = { exp = it }, label = { Text("Uscite (€)") })
            }
        }
    )
}

private fun formatDate(ms: Long, zone: ZoneId): String =
    Instant.ofEpochMilli(ms).atZone(zone).toLocalDate().toString()

private fun formatEuro(cents: Long): String =
    "€ " + String.format("%.2f", cents / 100.0)
