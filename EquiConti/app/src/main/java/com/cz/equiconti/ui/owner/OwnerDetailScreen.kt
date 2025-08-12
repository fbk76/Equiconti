package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
    // Carica l'owner dal ViewModel
    val owner: Owner? = vm.owners.firstOrNull { it.id == ownerId }

    var firstName by remember(owner) { mutableStateOf(TextFieldValue(owner?.firstName ?: "")) }
    var lastName by remember(owner) { mutableStateOf(TextFieldValue(owner?.lastName ?: "")) }
    var phone by remember(owner) { mutableStateOf(TextFieldValue(owner?.phone ?: "")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettaglio proprietario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
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

                    IconButton(
                        onClick = {
                            vm.removeOwner(ownerId)
                            onDelete()
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Elimina")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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
                label = { Text("Telefono (opzionale)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
