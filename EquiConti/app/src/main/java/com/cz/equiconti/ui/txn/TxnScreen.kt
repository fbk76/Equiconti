package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Txn
import com.cz.equiconti.ui.owner.OwnersViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Lista movimenti (entrate/uscite) per un proprietario.
 * Usa OwnersViewModel per leggere il flusso delle transazioni.
 */
@Composable
fun TxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddTxn: (ownerId: Long) -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    // titolo con owner
    var owner by remember { mutableStateOf<Owner?>(null) }
    LaunchedEffect(ownerId) {
        owner = vm.getOwnerById(ownerId)
    }

    val txns by vm.observeTxns(ownerId).collectAsState(initial = emptyList())
    val balance = remember(txns) {
        txns.fold(0L) { acc, t -> acc + (t.incomeCents - t.expenseCents) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(owner?.let { "Movimenti • ${it.lastName} ${it.firstName}" } ?: "Movimenti")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    // saldo
                    val euro = centsToCurrency(balance)
                    Text(
                        euro,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddTxn(ownerId) }) {
                Icon(Icons.Filled.Add, contentDescription = "Nuovo movimento")
            }
        }
    ) { padding ->
        if (txns.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessun movimento registrato.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(txns) { t ->
                    TxnRow(t)
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun TxnRow(t: Txn) {
    val dateStr = millisToDate(t.dateMillis)
    val amount = (t.incomeCents - t.expenseCents)
    val formatted = centsToCurrency(amount)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(dateStr, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                t.note ?: if (t.incomeCents > 0) "Entrata" else "Uscita",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        Text(
            text = formatted,
            style = MaterialTheme.typography.titleMedium,
            color = if (amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
    }
}

/* ===== util ===== */

private fun millisToDate(millis: Long): String {
    val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return fmt.format(Date(millis))
}

private fun centsToCurrency(cents: Long): String {
    val nf = NumberFormat.getCurrencyInstance()
    // NumberFormat usa la currency del locale; i centesimi vanno convertiti
    return nf.format(cents / 100.0)
}
