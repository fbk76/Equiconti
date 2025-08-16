package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Txn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    horseId: Long,
    onBack: () -> Unit
) {
    // TODO: collega al tuo ViewModel; qui solo struttura base
    val sample = emptyList<Txn>()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Movimenti") }) }
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {
            items(sample) { t ->
                Text("Txn id=${t.id}  amount=${t.amountCents}")
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
