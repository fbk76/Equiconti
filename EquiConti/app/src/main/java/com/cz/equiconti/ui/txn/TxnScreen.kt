package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Txn

@Composable
fun TxnScreen(
    vm: TxnViewModel = hiltViewModel()
) {
    // ✅ NON si invoca come funzione: è uno StateFlow
    val txns: List<Txn> by vm.txns.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Movimenti") }) }
    ) { pad ->
        if (txns.isEmpty()) {
            Box(Modifier.padding(pad).padding(16.dp)) { Text("Nessun movimento") }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(txns) { t ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(t.note ?: "—", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Importo: ${formatEuro(t.amountCents)}")
                        }
                    }
                }
            }
        }
    }
}

private fun formatEuro(cents: Long): String {
    val v = cents / 100.0
    return "€ " + String.format("%.2f", v)
}
