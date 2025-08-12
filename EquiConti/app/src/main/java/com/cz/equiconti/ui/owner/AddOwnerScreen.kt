package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOwnerScreen(
    navController: NavController,
    vm: OwnersViewModel = hiltViewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo proprietario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    TextButton(
                        enabled = firstName.isNotBlank() && lastName.isNotBlank(),
                        onClick = {
                            vm.saveOwner(
                                Owner(
                                    firstName = firstName.trim(),
                                    lastName = lastName.trim(),
                                    phone = phone.trim().ifBlank { null }
                                )
                            )
                            navController.popBackStack()
                        }
                    ) { Text("Salva") }
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
