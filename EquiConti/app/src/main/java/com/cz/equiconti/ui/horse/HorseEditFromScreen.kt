@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorseEditScreen(
    initialName: String = "",
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cavallo") }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome cavallo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { onSave(name) }, enabled = name.isNotBlank()) {
                    Text("Salva")
                }
                OutlinedButton(onClick = onCancel) {
                    Text("Annulla")
                }
            }
        }
    }
}
