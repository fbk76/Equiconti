@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Schermata di creazione/modifica cavallo.
 *
 * @param horseId   se non null, stai modificando un cavallo esistente (carica i dati a monte)
 * @param onSave    callback con i valori inseriti (name, breed, year)
 * @param onBack    callback per tornare indietro
 */
@Composable
fun HorseEditFormScreen(
    horseId: Long? = null,
    onSave: (name: String, breed: String?, year: Int?) -> Unit,
    onBack: () -> Unit,
    // opzionale: valori iniziali se li passi da VM
    initialName: String = "",
    initialBreed: String? = null,
    initialYear: Int? = null,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var breed by rememberSaveable { mutableStateOf(initialBreed ?: "") }
    var yearText by rememberSaveable { mutableStateOf(initialYear?.toString() ?: "") }

    val canSave = name.isNotBlank()

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
                            val year = yearText.toIntOrNull()
                            onSave(name.trim(), breed.trim().ifEmpty { null }, year)
                        },
                        enabled = canSave
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
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Razza (opz.)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = yearText,
                onValueChange = { yearText = it.filter { ch -> ch.isDigit() }.take(4) },
                label = { Text("Anno nascita (opz.)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val year = yearText.toIntOrNull()
                    onSave(name.trim(), breed.trim().ifEmpty { null }, year)
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salva")
            }
        }
    }
}
