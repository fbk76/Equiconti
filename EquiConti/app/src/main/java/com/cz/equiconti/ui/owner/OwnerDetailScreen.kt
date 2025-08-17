package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,                 // lasciato per compatibilità col NavGraph
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenTxns: () -> Unit,
    vm: OwnerDetailViewModel = hiltViewModel()
) {
    val horses by vm.horses.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Proprietario #$ownerId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Azioni principali
            Button(
                onClick = onAddHorse,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Aggiungi cavallo") }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onOpenTxns,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Vedi movimenti") }

            Spacer(Modifier.height(24.dp))

            Text("Cavalli", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (horses.isEmpty()) {
                Text("Nessun cavallo per questo proprietario.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(horses, key = { it.id }) { h ->
                        HorseRow(h)
                    }
                }
            }
        }
    }
}

@Composable
private fun HorseRow(horse: Horse) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(horse.name, style = MaterialTheme.typography.titleMedium)
            if (horse.monthlyFeeCents > 0) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Quota mensile: ${horse.monthlyFeeCents / 100}.${(horse.monthlyFeeCents % 100).toString().padStart(2, '0')} €",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            horse.notes?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(4.dp))
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@HiltViewModel
class OwnerDetailViewModel @Inject constructor(
    repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val ownerId: Long = savedStateHandle.get<Long>("ownerId") ?: 0L

    // flusso cavalli del proprietario
    val horses: StateFlow<List<Horse>> =
        repo.getHorses(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
