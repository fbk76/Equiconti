package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

@Composable
fun AddOwnerScreen(
    nav: NavController,
    ownerId: Long? = null,
    vm: OwnersViewModel = hiltViewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Se è edit, carica i dati
    LaunchedEffect(ownerId) {
        if (ownerId != null) {
            vm.loadOwner(ownerId)
        }
    }

    val current = vm.currentOwner.collectAsState().value
    LaunchedEffect(current) {
        current?.let {
            firstName = it.firstName
            lastName = it.lastName
            phone = it.phone ?: ""
        }
    }

    Column(Modifier.padding(16.dp)) {
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
            label = { Text("Telefono") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                vm.upsert(
                    Owner(
                        id = current?.id ?: 0L,
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone.ifBlank { null }
                    )
                ) {
                    nav.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salva")
        }
    }
}
