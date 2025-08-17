package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save

/**
 * Schermata di inserimento/modifica cavallo.
 *
 * @param horseId   se null -> nuovo cavallo, altrimenti modifica
 * @param onBack    torna alla schermata precedente
 * @param onSubmit  callback con i dati salvati (name, note)
 */
@Composable
fun HorseEditFormScreen(
    horseId: Long? = null,
    onBack: () -> Unit,
    onSubmit: (name: String, note: String) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var note by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (horseId == null) "Nuovo cavallo" else "Modifica cavallo")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSubmit(name.text.trim(), note.text.trim())
                            onBack()
                        }
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = "Salva")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome cavallo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    onSubmit(name.text.trim(), note.text.trim())
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salva")
            }
        }
    }
}
