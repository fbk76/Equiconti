package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OwnerDetailScreen(
    ownerFlow: Flow<Owner?>,
    onBack: () -> Unit,
    onEdit: (Owner) -> Unit,
    onDelete: (Owner) -> Unit,
    onOpenHorses: () -> Unit,
    onOpenTxns: () -> Unit
) {
    var owner by remember { mutableStateOf<Owner?>(null) }

    LaunchedEffect(Unit) {
        ownerFlow.collectLatest { owner = it }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Dettagli") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Indietro") }
                }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            if (owner == null) {
                Text("Proprietario non trovato.")
                return@Column
            }
            val o = owner!!
            Text("${o.firstName} ${o.lastName}", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Telefono: ${o.phone ?: "-"}")

            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onOpenHorses) { Text("Cavalli") }
                Button(onClick = onOpenTxns) { Text("Movimenti") }
            }

            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { onEdit(o) }) { Text("Modifica") }
                OutlinedButton(
                    onClick = { onDelete(o) },
                    colors = ButtonDefaults.outlinedButtonColors()
                ) { Text("Elimina") }
            }
        }
    }
}
