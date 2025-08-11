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

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Nuovo proprietario", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(value = first, onValueChange = { first = it }, label = { Text("Nome") })
            OutlinedTextField(value = last, onValueChange = { last = it }, label = { Text("Cognome") })
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefono (opz.)") })

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { nav.popBackStack() }) { Text("Annulla") }
                Button(onClick = {
                    onSave(Owner(firstName = first, lastName = last, phone = phone.ifBlank { null }))
                    nav.popBackStack() // torna all’elenco
                }) { Text("Salva") }
            }
        }
    }
}
