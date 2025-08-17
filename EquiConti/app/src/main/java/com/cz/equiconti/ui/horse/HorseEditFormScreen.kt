package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Schermata di edit/creazione cavallo.
 * - Se horseId == null => creazione
 * - Altrimenti => modifica
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseEditFormScreen(
    horseId: Long?,
    onBack: () -> Unit,
    onSaved: () -> Unit = {}
) {
    // --- Stato locale di esempio (sostituisci con il tuo ViewModel, se già presente) ---
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }

    // DatePicker (Material3 è ancora experimental)
    val datePickerState = rememberDatePickerState() // millis (Long?) selezionati

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (horseId == null) "Nuovo cavallo" else "Modifica cavallo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO salva i dati (usa il tuo ViewModel/repository)
                            onSaved()
                            onBack()
                        }
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = "Salva")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text
                )
            )

            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Razza") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Colore") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Se vuoi il selettore data di nascita
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Data di nascita", style = MaterialTheme.typography.titleMedium)
                    DatePicker(state = datePickerState)
                    val selected = datePickerState.selectedDateMillis
                    Text(
                        text = if (selected != null) "Millis selezionati: $selected" else "Nessuna data selezionata",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    // TODO salva i dati (usa il tuo ViewModel/repository)
                    onSaved()
                    onBack()
                },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Icon(Icons.Filled.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salva")
            }
        }
    }
}
