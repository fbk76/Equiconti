package com.cz.equiconti.ui.txn

import android.content.Intent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Txn
import com.cz.equiconti.util.exportTxnsPdf
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToLong

@Composable
fun TxnScreen(
    ownerName: String,
    ownerHorses: List<String>,
    txns: List<Txn>,
    onAddTxn: (dateMillis: Long, operation: String, incomeCents: Long, expenseCents: Long) -> Unit,
) {
    val context = LocalContext.current

    // Filtri data
    var from by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) } // 1° del mese
    var to by remember { mutableStateOf(LocalDate.now()) }

    // Dialog nuovo movimento
    var showAdd by remember { mutableStateOf(false) }

    val zone = ZoneId.systemDefault()
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val filtered = remember(txns, from, to) {
        val fromMs = from.atStartOfDay(zone).toInstant().toEpochMilli()
        val toMs = to.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        txns.filter { it.dateMillis in fromMs..toMs }.sortedBy { it.dateMillis }
    }

    val running = remember(filtered) {
        val accList = mutableListOf<Long>()
        var acc = 0L
        filtered.forEach { t ->
            acc += t.incomeCents - t.expenseCents
            accList.add(acc)
        }
        accList
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti • $ownerName") },
                actions = {
                    IconButton(onClick = {
                        try {
                            val uri = exportTxnsPdf(
                                context = context,
                                ownerName = ownerName,
                                horses = ownerHorses,
                                txns = filtered,
                                fromLabel = from.format(fmt),
                                toLabel = to.format(fmt),
                                fileName = "Report_${ownerName.replace(' ', '_')}"
                            )
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        } catch (_: Throwable) {
                            // opzionale: mostrare un Toast in caso di errore
                        }
                    }) {
                        Icon(Icons.Filled.PictureAsPdf, contentDescription = "Stampa/Esporta")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(Icons.Filled.Save, contentDescription = "Aggiungi")
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(12.dp)) {
            // Cavalli posseduti
            if (ownerHorses.isNotEmpty()) {
                Text(
                    "Cavalli: " + ownerHorses.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
            }

            // Filtri date
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

            // Righe
            LazyColumn {
                items(filtered.indices) { idx ->
                    val t = filtered[idx]
                    val saldo = running[idx]
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BodyCell(formatDate(t.dateMillis, zone, fmt), 1.0f)
                        BodyCell(t.operation, 2.2f)
                        BodyCell(formatEuro(t.incomeCents), 1.0f)
                        BodyCell(formatEuro(t.expenseCents), 1.0f)
                        BodyCell(formatEuro(saldo), 1.0f, bold = true)
                    }
                    Divider()
                }
            }

            // Totali
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
private fun HeaderCell(text: String, weight: Float) {
    Text(text, modifier = Modifier.weight(weight), fontWeight = FontWeight.SemiBold)
}

@Composable
private fun BodyCell(text: String, weight: Float, bold: Boolean = false) {
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

private fun formatDate(ms: Long, zone: ZoneId, fmt: DateTimeFormatter): String {
    return Instant.ofEpochMilli(ms).atZone(zone).toLocalDate().format(fmt)
}

private fun formatEuro(cents: Long): String {
    val s = cents / 100.0
    return "€ " + String.format("%.2f", s)
}
