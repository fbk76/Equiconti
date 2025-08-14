package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(onClick = onAddHorse) {
                        Icon(Icons.Filled.Add, contentDescription = "Aggiungi cavallo")
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
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            val filtered = if (query.isBlank()) horses
            else horses.filter { it.name.contains(query, ignoreCase = true) }

            if (filtered.isEmpty()) {
                Text("Nessun cavallo", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    items(filtered, key = { it.id }) { horse ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenHorse(horse) }
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = horse.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
