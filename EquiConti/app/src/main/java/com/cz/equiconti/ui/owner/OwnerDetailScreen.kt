package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun OwnerDetailScreen(
    nav: NavController,
    ownerId: Long,
    vm: OwnersViewModel = hiltViewModel()
) {
    LaunchedEffect(ownerId) {
        vm.loadOwner(ownerId)
    }

    val owner = vm.currentOwner.collectAsState().value

    Column(Modifier.padding(16.dp)) {
        owner?.let {
            Text("Nome: ${it.firstName}")
            Text("Cognome: ${it.lastName}")
            Text("Telefono: ${it.phone ?: "-"}")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { nav.navigate("editOwner/${it.id}") }) {
                Text("Modifica")
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    vm.delete(it) { nav.popBackStack() }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Elimina")
            }
        } ?: Text("Proprietario non trovato")
    }
}
