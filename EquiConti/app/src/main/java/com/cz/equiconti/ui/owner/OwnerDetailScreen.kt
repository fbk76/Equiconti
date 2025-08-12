package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner
import kotlinx.coroutines.launch

@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onEdit: (Owner) -> Unit = {},
    onOpenHorses: (Long) -> Unit,
    onOpenTxns: (Long) -> Unit,
    onDeleted: () -> Unit,
    vm: OwnersViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    var owner by remember { mutableStateOf<Owner?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(ownerId) {
        owner = vm.getOwnerById(ownerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettagli") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    if (owner != null) {
                        IconButton(onClick = { onEdit(owner!!) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Modifica")
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Elimina")
                        }
                    }
                }
            )
        }
    ) { padding ->
        val o = owner
        if (o == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Proprietario non trovato.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Text(
                    "${o.lastName} ${o.firstName}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (!o.phone.isNullOrBlank()) {
                    Text("Telefono: ${o.phone}", style = MaterialTheme.typography.bodyLarge)
                }

                // Azioni principali
                ElevatedButton(
                    onClick = { onOpenHorses(o.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Pets, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cavalli")
                }

                ElevatedButton(
                    onClick = { onOpenTxns(o.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.List, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Movimenti (Entrate/Uscite)")
                }
            }
        }

        if (showDeleteConfirm && owner != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Elimina proprietario") },
                text = { Text("Vuoi davvero eliminare ${owner!!.lastName} ${owner!!.firstName}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirm = false
                            scope.launch {
                                vm.deleteOwner(owner!!)
                                onDeleted()
                            }
                        }
                    ) { Text("Elimina") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) { Text("Annulla") }
                }
            )
        }
    }
}
