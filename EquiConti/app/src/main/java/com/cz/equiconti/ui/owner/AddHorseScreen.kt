package com.cz.equiconti.ui.owner
package com.cz.equiconti.ui.owner

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction

@Composable
fun AddHorseScreen(
    ownerId: Long,
    onBack: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    var name    by remember { mutableStateOf("") }
    var feeEuro by remember { mutableStateOf("") }
    var notes   by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Nuovo cavallo") }) }) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Nome cavallo") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = feeEuro,
                onValueChange = { feeEuro = it },
                label = { Text("Quota mensile (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(notes, { notes = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack) { Text("Annulla") }
                Button(
                    onClick = {
                        val cents = ((feeEuro.replace(',', '.').toDoubleOrNull() ?: 0.0) * 100).roundToLong()
                        vm.upsertHorse(
                            Horse(
                                id = 0L,
                                ownerId = ownerId,
                                name = name.trim(),
                                monthlyFeeCents = cents,
                                notes = notes.trim().ifBlank { null }
                            )
                        )
                        onBack()
                    },
                    enabled = name.isNotBlank()
                ) { Text("Salva") }
            }
        }
    }
}
