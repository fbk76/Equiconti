package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Txn
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    ownerId: Long,                 // compatibile col tuo NavGraph
    onBack: () -> Unit,
    vm: TxnViewModel = hiltViewModel()
) {
    val txns by vm.txns.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Movimenti proprietario #$ownerId") }) }
    ) { pad ->
        if (txns.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Nessun movimento",
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(txns, key = { it.id }) { t ->
                    TxnRow(t)
                }
            }
        }
    }
}

@Composable
private fun TxnRow(t: Txn) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("ID: ${t.id}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            val amount = "${t.amountCents / 100}.${(t.amountCents % 100).toString().padStart(2, '0')} €"
            Text("Importo: $amount", style = MaterialTheme.typography.titleMedium)
            t.notes?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(4.dp))
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@HiltViewModel
class TxnViewModel @Inject constructor(
    repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val ownerId: Long = savedStateHandle.get<Long>("ownerId") ?: 0L

    // flusso movimenti del proprietario
    val txns: StateFlow<List<Txn>> =
        repo.getTxns(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
