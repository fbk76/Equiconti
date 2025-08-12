package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner

@Composable
fun OwnersScreen(
    owners: List<Owner>,
    onOwnerClick: (Owner) -> Unit,
    onAddOwner: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddOwner) { Text("+") }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("Proprietari", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            owners.forEach { o ->
                ListItem(
                    headlineContent = { Text("${o.firstName} ${o.lastName}") },
                    supportingContent = { if (!o.phone.isNullOrBlank()) Text(o.phone!!) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOwnerClick(o) }
                )
                Divider()
            }
            if (owners.isEmpty()) {
                Text("Nessun proprietario. Tocca + per aggiungerne uno.")
            }
        }
    }
}
