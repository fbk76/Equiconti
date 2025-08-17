package com.cz.equiconti.ui.horse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Schermata di creazione/modifica cavallo.
 *
 * @param horseId  id del cavallo se modifica, altrimenti null (creazione)
 * @param initialName   valore iniziale nome
 * @param initialBreed  razza iniziale (facoltativo)
 * @param initialYear   anno di nascita iniziale (facoltativo)
 * @param initialNotes  note iniziali (facoltativo)
 * @param onBack   callback per tornare indietro
 * @param onSave   callback di salvataggio: (name, breed, year, notes) -> Unit
 */
@OptIn(ExperimentalMaterial3Api::class) // solo per TopAppBar (stabile ma richiede opt-in in alcune versioni)
@Composable
fun HorseEditFormScreen(
    horseId: Long? = null,
    initialName: String = "",
    initialBreed: String = "",
    initialYear: Int? = null,
    initialNotes: String = "",
    onBack: () -> Unit = {},
    onSave: (name: String, breed: String?, year: Int?, notes: String?) -> Unit = { _, _, _, _ -> }
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var breed by rememberSaveable { mutableStateOf(initialBreed) }
    var yearText by rememberSaveable { mutableStateOf(initialYear?.toString() ?: "") }
    var notes by rememberSaveable { mutableStateOf(initialNotes) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (horseId == null) "Nuovo cavallo" else "Modifica cavallo") },
                navigationIcon = { /* se vuoi inserire un icona Back in futuro */ },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .then(Modifier.padding(padding))
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Nome (obbligatorio)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome*") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Razza (facoltativa)
            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Razza (opz.)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Anno di nascita numerico e max 4 cifre
            OutlinedTextField(
                value = yearText,
                onValueChange = { newValue ->
                    // Solo cifre e max 4
                    yearText = newValue.filter { ch -> ch.isDigit() }.take(4)
                },
                label = { Text("Anno nascita (opz.)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Note
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Note (opz.)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val yearInt = yearText.toIntOrNull()
                    val breedOut = breed.ifBlank { null }
                    val notesOut = notes.ifBlank { null }
                    onSave(name.trim(), breedOut, yearInt, notesOut)
                    onBack()
                },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salva")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Annulla")
            }
        }
    }
}
