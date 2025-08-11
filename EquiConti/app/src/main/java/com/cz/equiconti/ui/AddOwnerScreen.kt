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
        topBar = { CenterAlignedTopAppBar(title = { Text("Nuovo proprietario") }) },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { nav.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) { Text("Annulla") }

                Button(
                    enabled = first.isNotBlank() && last.isNotBlank(),
                    onClick = {
                        onSave(
                            Owner(
                                firstName = first.trim(),
                                lastName = last.trim(),
                                phone = phone.trim().ifBlank { null }
                            )
                        )
                        nav.popBackStack() // torna alla lista che osserverà il Flow e si aggiornerà
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Salva") }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = first, onValueChange = { first = it }, label = { Text("Nome") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = last, onValueChange = { last = it }, label = { Text("Cognome") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefono (opz.)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        }
    }
}
