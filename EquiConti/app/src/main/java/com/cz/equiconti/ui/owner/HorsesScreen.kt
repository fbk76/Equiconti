package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Horse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorsesScreen(
    ownerName: String,
    horses: List<Horse>,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenHorse: (Horse) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Cavalli • $ownerName") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAddHorse) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Aggiungi cavallo"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Cerca per nome") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )

            Spacer(Modifier.height(12.dp))

            val filtered = remember(query, horses) {
                if (query.isBlank()) horses
                else horses.filter { it.name.contains(query, ignoreCase = true) }
            }

            if (filtered.isEmpty()) {
                Text(
                    text = if (horses.isEmpty()) "Nessun cavallo" else "Nessun risultato",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn {
                    items(filtered, key = { it.id }) { horse ->
                        HorseRow(
                            horse = horse,
                            onClick = { onOpenHorse(horse) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun HorseRow(
    horse: Horse,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(text = horse.name, style = MaterialTheme.typography.titleMedium)
            val subtitle = listOfNotNull(horse.breed, horse.color).filter { it.isNotBlank() }.joinToString(" • ")
            if (subtitle.isNotBlank()) {
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
