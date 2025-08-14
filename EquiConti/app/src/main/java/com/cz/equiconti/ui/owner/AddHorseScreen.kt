package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
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
    val canSave = name.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo cavallo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (canSave) {
                            onSave(
                                Horse(
                                    id = 0,
                                    ownerId = ownerId,
                                    name = name.trim()
                                )
                            )
                        }
                    }, enabled = canSave) {
                        Icon(Icons.Filled.Save, contentDescription = "Salva")
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
                singleLine = true
            )

            Button(
                onClick = {
                    onSave(
                        Horse(
                            id = 0,
                            ownerId = ownerId,
                            name = name.trim()
                        )
                    )
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Salva") }
        }
    }
}
