package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse

@Composable
fun AddHorseScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("") } // euro

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Nuovo cavallo")

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Note (opz.)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = fee, onValueChange = { fee = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
            label = { Text("Quota mensile (€)") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onBack) { Text("Annulla") }
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    val cents = ((fee.replace(',', '.').toDoubleOrNull() ?: 0.0) * 100).toLong()
                    vm.upsertHorse(
                        Horse(
                            id = 0L,
                            ownerId = ownerId,
                            name = name.trim(),
                            monthlyFeeCents = cents,
                            notes = notes.trim().ifBlank { null }
                        )
                    )
                    onSaved()
                }
            ) { Text("Salva") }
        }
    }
}
