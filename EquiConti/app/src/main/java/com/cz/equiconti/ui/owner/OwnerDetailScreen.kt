package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner

@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onOpenHorses: (Long) -> Unit,
    onOpenTxns: (Long) -> Unit,
    onDeleted: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owner by vm.ownerFlow(ownerId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Dettagli") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { padding ->
        if (owner == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Proprietario non trovato.")
            }
        } else {
            val o: Owner = owner!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "${o.lastName} ${o.firstName}",
                    style = MaterialTheme.typography.titleLarge
                )
                if (!o.phone.isNullOrBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(o.phone!!)
                }

                Spacer(Modifier.height(24.dp))

                Button(onClick = { onOpenHorses(o.id) }) {
                    Text("Cavalli")
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { onOpenTxns(o.id) }) {
                    Text("Movimenti")
                }

                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = { onEdit(o.id) }) {
                        Text("Modifica")
                    }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            vm.deleteOwner(o)
                            onDeleted()
                        }
                    ) {
                        Text("Elimina")
                    }
                }
            }
        }
    }
}
