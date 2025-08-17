package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    vm: TxnViewModel = hiltViewModel()
) {
    val txns by vm.txns(ownerId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Filled.Add, contentDescription = "Aggiungi movimento")
            }
        }
    ) { pad ->
        if (txns.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Nessun movimento.", style = MaterialTheme.typography.bodyMedium)
                Text("Tocca + per aggiungerne uno.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
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
    val dateStr = formatDate(t.createdAt)
    val amountStr = formatEuro(t.amountCents)
    val amountColor = if (t.amountCents >= 0) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(dateStr, style = MaterialTheme.typography.labelMedium)
            if (!t.notes.isNullOrBlank()) {
                Text(t.notes!!, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                amountStr,
                style = MaterialTheme.typography.titleMedium,
                color = amountColor
            )
        }
    }
}

/* --------------------------- ViewModel --------------------------- */

@HiltViewModel
class TxnViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /** Restituisce i movimenti per tutti i cavalli dell'owner (ordine come esce dal DAO). */
    fun txns(ownerId: Long): Flow<List<Txn>> = repo.getTxns(ownerId)
}

/* --------------------------- Utils --------------------------- */

private fun formatDate(millis: Long): String {
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().format(fmt)
}

private fun formatEuro(cents: Long): String {
    val sign = if (cents < 0) "-" else "+"
    val absValue = abs(cents) / 100.0
    return "$sign€ ${"%.2f".format(absValue)}"
}
