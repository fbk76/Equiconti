package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner
import com.cz.equiconti.ui.owner.OwnersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquiContiApp(
    vm: OwnersViewModel = hiltViewModel(),
    onAddOwnerClick: () -> Unit = {},          // callback facoltativa per aprire una schermata di inserimento
    onOwnerSelected: (Owner) -> Unit = {}      // callback facoltativa per aprire il dettaglio
) {
    // IMPORTANTE: diamo un initial esplicito così il tipo è chiaro (List<Owner>)
    val owners by vm.ownersFlow().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EquiConti") }
            )
        },
        floatingActionButton = {
            // Non chiamiamo funzioni inesistenti del VM (es. addOwner): esponiamo un semplice callback
            FloatingActionButton(onClick = onAddOwnerClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Aggiungi proprietario")
            }
        }
    ) { pad ->
        OwnersList(
            owners = owners,
            onClick = onOwnerSelected,
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Composable
private fun OwnersList(
    owners: List<Owner>,
    onClick: (Owner) -> Unit,
    modifier: Modifier = Modifier
) {
    // QUI usiamo .isEmpty() su una LISTA (nessuna ambiguità)
    if (owners.isEmpty()) {
        Box(modifier) { Text("Nessun proprietario. Tocca + per aggiungerne uno.") }
        return
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(owners) { owner ->
            ElevatedCard(
                onClick = { onClick(owner) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(owner.name, style = MaterialTheme.typography.titleMedium)
                    if (!owner.phone.isNullOrBlank()) {
                        Text(owner.phone!!)
                    }
                }
            }
        }
    }
}
