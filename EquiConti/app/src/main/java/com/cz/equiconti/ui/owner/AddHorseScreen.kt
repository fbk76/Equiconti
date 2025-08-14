package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Horse
import kotlin.math.roundToLong

@Composable
fun AddHorseScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onSave: ((Horse) -> Unit)? = null
) {
    var name by remember { mutableStateOf("") }
    var feeEuro by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuovo cavallo") }) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Nome cavallo") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = feeEuro,
                onValueChange = { feeEuro = it },
                label = { Text("Quota mensile (€)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(notes, { notes = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onBack) { Text("Annulla") }
                Button(
                    onClick = {
                        val cents = ((feeEuro.replace(',', '.').toDoubleOrNull() ?: 0.0) * 100).roundToLong()
                        val horse = Horse(
                            id = 0L,
                            ownerId = ownerId,
                            name = name.trim(),
                            monthlyFeeCents = cents,
                            notes = notes.trim().ifBlank { null }
                        )
                        onSave?.invoke(horse)
                        onBack()
                    },
                    enabled = name.isNotBlank()
                ) { Text("Salva") }
            }
        }
    }
}
