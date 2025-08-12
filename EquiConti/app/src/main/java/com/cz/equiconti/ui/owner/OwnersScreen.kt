package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun OwnersScreen(
    owners: StateFlow<List<com.cz.equiconti.data.Owner>>,
    onOwnerClick: (Long) -> Unit,
    onAddOwner: () -> Unit
) {
    val list = owners.collectAsState().value
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onAddOwner, text = { Text("+") })
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("Proprietari", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            list.forEach { o ->
                Text(
                    text = "${o.lastName} ${o.firstName}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOwnerClick(o.id) }
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}
