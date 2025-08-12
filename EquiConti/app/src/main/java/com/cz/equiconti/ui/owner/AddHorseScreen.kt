package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.ui.owner.OwnersViewModel

@Composable
fun AddHorseScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Nuovo cavallo")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome cavallo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (opzionale)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onBack) { Text("Annulla") }
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    // Costruisci l’oggetto Horse secondo il tuo data class
                    val horse = Horse(
                        id = 0L,            // autoGenerate
                        ownerId = ownerId,
                        name = name,
                        note = if (note.isBlank()) null else note
                    )
                    vm.upsertHorse(horse)
                    onSaved()
                }
            ) { Text("Salva") }
        }
    }
}
