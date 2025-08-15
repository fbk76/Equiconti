package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner

@Composable
fun OwnersListScreen(
    owners: List<Owner>,
    onAddOwner: () -> Unit,
    onOpenOwner: (Owner) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Proprietari") }, actions = {
                TextButton(onClick = onAddOwner) { Text("Nuovo") }
            })
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(owners) { o ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenOwner(o) }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${o.lastName} ${o.firstName}", style = MaterialTheme.typography.titleMedium)
                        if (!o.phone.isNullOrBlank()) {
                            Text(o.phone!!, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
