package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 * Schermata di creazione/modifica cavallo.
 *
 * @param ownerId  id proprietario (contesto)
 * @param horseId  id cavallo (null => nuovo)
 * @param onBack   callback per tornare indietro
 */
@Composable
fun HorseEditFormScreen(
    ownerId: Long,
    horseId: Long?,
    onBack: () -> Unit
) {
    // Stati minimi per compilare (sostituisci poi con il tuo VM)
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var color by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (horseId == null) "Nuovo cavallo" else "Modifica cavallo") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Indietro") }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Mantello") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = onBack) { Text("Salva") }
            }
        }
    }
}
