package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner

@Composable
fun OwnerDialog(onDismiss: () -> Unit, onSave: (Owner) -> Unit) {
    var surname by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(Owner(surname = surname, name = name, phone = phone.ifBlank { null }))
                },
                enabled = surname.isNotBlank() && name.isNotBlank()
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
        title = { Text("Nuovo cliente") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(surname, { surname = it }, label = { Text("Cognome") }, singleLine = true)
                OutlinedTextField(name, { name = it }, label = { Text("Nome") }, singleLine = true)
                OutlinedTextField(phone, { phone = it }, label = { Text("Telefono") }, singleLine = true)
            }
        }
    )
}
