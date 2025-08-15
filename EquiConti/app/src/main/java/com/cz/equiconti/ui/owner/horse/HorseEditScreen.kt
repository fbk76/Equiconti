package com.cz.equiconti.ui.horse

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorseEditScreen(
    ownerId: Long,
    onCancel: () -> Unit,
    onSaved: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Cavallo di #$ownerId") }) }
    ) { pad ->
        Column(
            Modifier
                .padding(pad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Razza (opz.)") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onSaved, enabled = name.isNotBlank()) { Text("Salva") }
                OutlinedButton(onClick = onCancel) { Text("Annulla") }
            }
        }
    }
}
