package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOwnerScreen(
    onSave: (Owner) -> Unit,
    onBack: () -> Unit
) {
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo proprietario") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Indietro") }
                },
                actions = {
                    val enabled = firstName.text.isNotBlank() && lastName.text.isNotBlank()
                    TextButton(onClick = {
                        onSave(
                            Owner(
                                id = 0L,
                                firstName = firstName.text.trim(),
                                lastName = lastName.text.trim(),
                                phone = phone.text.trim().ifBlank { null }
                            )
                        )
                    }, enabled = enabled) { Text("Salva") }
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
            Spacer(Modifier.weight(1f))
            ElevatedButton(
                onClick = {
                    onSave(
                        Owner(
                            id = 0L,
                            firstName = firstName.text.trim(),
                            lastName = lastName.text.trim(),
                            phone = phone.text.trim().ifBlank { null }
                        )
                    )
                },
                enabled = firstName.text.isNotBlank() && lastName.text.isNotBlank(),
                modifier = Modifier.align(Alignment.End)
            ) { Text("Salva") }
        }
    }
}
