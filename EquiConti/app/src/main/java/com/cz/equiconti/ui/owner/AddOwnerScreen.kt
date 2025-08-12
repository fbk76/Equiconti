package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner

@Composable
fun AddOwnerScreen(
    onSave: (Owner) -> Unit,
    onBack: () -> Unit
) {
    var first by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Nuovo proprietario") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Indietro") }
                },
                actions = {
                    TextButton(
                        enabled = first.isNotBlank() && last.isNotBlank(),
                        onClick = {
                            onSave(Owner(firstName = first, lastName = last, phone = phone.ifBlank { null }))
                        }
                    ) { Text("Salva") }
                }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = first, onValueChange = { first = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = last,  onValueChange = { last  = it }, label = { Text("Cognome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefono") }, modifier = Modifier.fillMaxWidth())
        }
    }
}
