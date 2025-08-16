package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseEditScreen(
    ownerId: Long,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuovo cavallo") }) }
    ) { pad ->
        Column(
            Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Razza (opz.)") })
            Spacer(Modifier.height(20.dp))
            Button(onClick = {
                // TODO: salva (usa il tuo VM se già presente)
                onBack()
            }) {
                Text("Salva")
            }
        }
    }
}
