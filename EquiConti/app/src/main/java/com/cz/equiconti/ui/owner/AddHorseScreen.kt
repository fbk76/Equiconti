package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Horse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHorseScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onSave: (Horse) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var breed by rememberSaveable { mutableStateOf("") }
    var color by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    val canSave = name.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo cavallo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (canSave) {
                            val y = year.toIntOrNull()
                            onSave(
                                Horse(
                                    id = 0,
                                    ownerId = ownerId,
                                    name = name.trim(),
                                    breed = breed.trim(),
                                    color = color.trim(),
                                    year = y,
                                    notes = notes.trim()
                                )
                            )
                        }
                    }, enabled = canSave) {
                        Icon(Icons.Default.Save, contentDescription = "Salva")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome *") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )

            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Razza") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )

            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Mantello") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )

            OutlinedTextField(
                value = year,
                onValueChange = { year = it.filter { ch -> ch.isDigit() }.take(4) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Anno di nascita") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                label = { Text("Note") }
            )

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    val y = year.toIntOrNull()
                    onSave(
                        Horse(
                            id = 0,
                            ownerId = ownerId,
                            name = name.trim(),
                            breed = breed.trim(),
                            color = color.trim(),
                            year = y,
                            notes = notes.trim()
                        )
                    )
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salva")
            }
        }
    }
}
