package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cz.equiconti.data.Owner

@Composable
fun OwnersScreen(
    onAddOwner: () -> Unit,
    onOwnerClick: (Long) -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owners by vm.owners.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddOwner) { Text("+") }
        },
        topBar = { CenterAlignedTopAppBar(title = { Text("Proprietari") }) }
    ) { padding ->
        if (owners.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessun proprietario. Premi + per aggiungerne uno.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(owners) { o ->
                    OwnerRow(o) { onOwnerClick(o.id) }
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun OwnerRow(o: Owner, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text("${o.lastName} ${o.firstName}") },
        supportingContent = { if (!o.phone.isNullOrBlank()) Text(o.phone!!) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
