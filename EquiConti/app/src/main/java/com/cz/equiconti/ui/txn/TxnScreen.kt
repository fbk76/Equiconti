package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Txn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    vm: TxnViewModel = hiltViewModel()
) {
    val txnsState = vm.txns.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti proprietario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { pad ->
        TxnListContent(pad, txnsState.value)
    }
}

@Composable
private fun TxnListContent(pad: PaddingValues, txns: List<Txn>) {
    LazyColumn(
        modifier = Modifier.padding(pad).padding(16.dp)
    ) {
        if (txns.isEmpty()) {
            item { Text("Nessun movimento registrato.") }
        } else {
            items(txns) { t ->
                Text("ID=${t.id}  importo=${t.amountCents}  note=${t.notes ?: ""}")
                Spacer(Modifier.height(8.dp))
                Divider()
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
