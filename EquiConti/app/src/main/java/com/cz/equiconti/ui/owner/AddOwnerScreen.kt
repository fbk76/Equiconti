package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

@Composable
fun AddOwnerScreen(
    nav: NavController,
    onSave: (Owner) -> Unit
) {
    var first by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuovo proprietario") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (first.isNotBlank() || last.isNotBlank()) {
                        onSave(Owner(firstName = first, lastName = last, phone = phone.ifBlank { null }))
                        nav.popBackStack()
                    }
                },
                text = { Text("Salva") }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            OutlinedTextField(first, { first = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(last, { last = it }, label = { Text("Cognome") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(phone, { phone = it }, label = { Text("Telefono (opz.)") }, modifier = Modifier.fillMaxWidth())
        }
    }
}
