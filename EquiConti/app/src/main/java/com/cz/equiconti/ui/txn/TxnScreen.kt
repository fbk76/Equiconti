package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Txn
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    horseId: Long,
    onBack: () -> Unit,
    vm: TxnViewModel = hiltViewModel()
) {
    val txns: List<Txn> by vm.txns(horseId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {
            if (txns.isEmpty()) {
                Text("Nessun movimento registrato.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(txns) { t ->
                        TxnRow(t)
                    }
                }
            }
        }
    }
}

@Composable
private fun TxnRow(t: Txn) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Importo: €${t.amountCents / 100.0}")
            t.note?.let { Text("Nota: $it") }
            // ✅ Fix: era scritto "typTypography"
            Text(formatDate(t.timestamp), style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun formatDate(ts: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(ts))
}
