package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    // carica l'owner dal VM
    val owner = remember(ownerId, vm.owners) {
        vm.owners.firstOrNull { it.id == ownerId }
    }

    var firstName by remember(owner) { mutableStateOf(TextFieldValue(owner?.firstName.orEmpty())) }
    var lastName  by remember(owner) { mutableStateOf(TextFieldValue(owner?.lastName.orEmpty())) }
    var phone     by remember(owner) { mutableStateOf(TextFieldValue(owner?.phone.orEmpty())) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proprietario") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Indietro") }
                },
                actions = {
                    // Salva
                    TextButton(
                        enabled = firstName.text.isNotBlank() && lastName.text.isNotBlank(),
                        onClick = {
                            val updated = Owner(
                                id = ownerId,
                                firstName = firstName.text.trim(),
                                lastName = lastName.text.trim(),
                                phone = phone.text.trim().ifBlank { null }
                            )
                            vm.saveOwner(updated)
                            onBack()
                        }
                    ) { Text("Salva") }

                    // Elimina
                    IconButton(
                        onClick = {
                            vm.removeOwner(ownerId)
                            onDelete()
                        }
                    ) { Icon(Icons.Default.Delete, contentDescription = "Elimina") }
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
                value = firstName, onValueChange = { firstName = it },
                label = { Text("Nome") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName, onValueChange = { lastName = it },
                label = { Text("Cognome") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                label = { Text("Telefono (opz.)") }, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
