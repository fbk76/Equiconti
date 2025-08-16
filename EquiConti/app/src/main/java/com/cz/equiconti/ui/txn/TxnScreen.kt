package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Txn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    horseId: Long,
    onBack: () -> Unit,
    vm: TxnViewModel = hiltViewModel()
) {
    val txns: List<Txn> by vm.txns(horseId).collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (txns.isEmpty()) {
                Text("Nessun movimento per questo cavallo.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(txns) { t: Txn -> TxnRow(t) }
                }
            }
        }
    }
}

@Composable
private fun TxnRow(t: Txn) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = formatEuro(t.amountCents),
                style = MaterialTheme.typography.titleMedium
            )
            t.note?.let { Text(it) }
            Text(formatDate(t.timestamp), style = MaterialTheme.typTypography.bodySmall)
        }
    }
}

private fun formatEuro(cents: Long): String {
    val v = cents / 100.0
    return "€ " + String.format(Locale.getDefault(), "%.2f", v)
}

private fun formatDate(millis: Long): String {
    val df = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return df.format(Date(millis))
}
