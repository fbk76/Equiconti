package com.cz.equiconti.ui.horse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.ui.owner.OwnersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseEditScreen(
    ownerId: Long,
    onCancel: () -> Unit,
    onSaved: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuovo cavallo") }) }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
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
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Razza (opzionale)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onCancel) { Text("Annulla") }
                Button(
                    onClick = {
                        vm.saveHorse(ownerId, name.trim(), breed.ifBlank { null })
                        onSaved()
                    },
                    enabled = name.isNotBlank()
                ) { Text("Salva") }
            }
        }
    }
}
