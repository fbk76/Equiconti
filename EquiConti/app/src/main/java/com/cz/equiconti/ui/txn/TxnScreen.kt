package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
    ownerId: Long,
    onBack: () -> Unit
) {
    // TODO: collega al tuo ViewModel e carica i movimenti per ownerId
    val sample = emptyList<Txn>()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Movimenti proprietario #$ownerId") }) }
    ) { pad: PaddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {
            items(sample) { t ->
                Text("Txn id=${t.id}  amount=${t.amountCents}")
                Spacer(Modifier.padding(8.dp))
            }
        }
    }
}
