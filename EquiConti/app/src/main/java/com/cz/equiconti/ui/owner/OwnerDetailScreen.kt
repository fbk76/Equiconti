package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    vm: OwnersViewModel
) {
    val current = vm.getOwnerById(ownerId)

    if (current == null) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Dettagli") }) }
        ) { inner ->
            Column(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Proprietario non trovato.")
                TextButton(onClick = onBack) { Text("Indietro") }
            }
        }
        return
    }

    var firstName by remember(current.id) { mutableStateOf(current.firstName) }
    var lastName  by remember(current.id) { mutableStateOf(current.lastName) }
    var phone     by remember(current.id) { mutableStateOf(current.phone ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettagli proprietario") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Indietro") }
                },
                actions = {
                    TextButton(
                        enabled = firstName.isNotBlank() && lastName.isNotBlank(),
                        onClick = {
                            vm.saveOwner(
                                current.copy(
                                    firstName = firstName.trim(),
                                    lastName = lastName.trim(),
                                    phone = phone.trim().ifBlank { null }
                                )
                            )
                            onBack()
                        }
                    ) { Text("Salva") }

                    TextButton(
                        onClick = {
                            vm.removeOwner(current.id)
                            onDelete()
                        }
                    ) { Text("Elimina") }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Cognome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Telefono (opz.)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
