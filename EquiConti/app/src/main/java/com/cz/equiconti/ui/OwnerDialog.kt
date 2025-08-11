package com.cz.equiconti.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner

/**
 * Dialog per creare/modificare un Owner.
 * - Usa i campi corretti: firstName, lastName, phone
 */
@Composable
fun OwnerDialog(
    initial: Owner? = null,
    onDismiss: () -> Unit,
    onSave: (Owner) -> Unit
) {
    var firstName by remember { mutableStateOf(initial?.firstName ?: "") }
    var lastName  by remember { mutableStateOf(initial?.lastName  ?: "") }
    var phone     by remember { mutableStateOf(initial?.phone     ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = firstName.isNotBlank() || lastName.isNotBlank(),
                onClick = {
                    val owner = Owner(
                        id = initial?.id ?: 0L,
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        phone = phone.trim().ifEmpty { null }
                    )
                    onSave(owner)
                }
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
        title = { Text(if (initial == null) "Nuovo proprietario" else "Modifica proprietario") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
    )
}
