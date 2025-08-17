package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnersScreen(
    onOwnerClick: (Long) -> Unit,
    onAddOwner: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owners by vm.owners.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Proprietari") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddOwner) {
                Icon(Icons.Filled.Add, contentDescription = "Nuovo proprietario")
            }
        }
    ) { inner ->
        if (owners.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessun proprietario. Tocca + per aggiungerne uno.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(owners, key = { it.id }) { owner ->
                    OwnerRow(
                        owner = owner,
                        onClick = { onOwnerClick(owner.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OwnerRow(
    owner: Owner,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(owner.name, style = MaterialTheme.typography.titleMedium)
            if (!owner.phone.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(owner.phone!!, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
