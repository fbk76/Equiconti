package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import kotlinx.coroutines.flow.map

/**
 * Schermata elenco cavalli per un proprietario.
 *
 * Requisiti:
 * - OwnersViewModel espone fun horses(ownerId: Long): Flow<List<Horse>>
 */
@Composable
fun HorsesScreen(
    ownerId: Long,
    ownerName: String?,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onHorseClick: (Horse) -> Unit,
    viewModel: OwnersViewModel = hiltViewModel()
) {
    // Ricerca locale
    var query by remember { mutableStateOf("") }

    // Colleziona i cavalli dal VM e filtra in base alla query
    val horses by viewModel
        .horses(ownerId)
        .map { list ->
            if (query.isBlank()) list
            else list.filter { h ->
                h.name.contains(query, ignoreCase = true) ||
                (h.notes?.contains(query, ignoreCase = true) == true)
            }
        }
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = ownerName?.let { "Cavalli di $it" } ?: "Cavalli") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(onClick = onAddHorse) {
                        Icon(Icons.Filled.Add, contentDescription = "Nuovo cavallo")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Campo ricerca (fix per KeyboardOptions)
            TextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                placeholder = { Text("Cerca cavallo…") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
            )

            if (horses.isEmpty()) {
                Text(
                    text = "Nessun cavallo.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(horses) { horse ->
                        HorseRow(horse = horse, onClick = { onHorseClick(horse) })
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
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = horse.name,
            style = MaterialTheme.typography.titleMedium
        )
        val subtitle = buildString {
            horse.breed?.takeIf { it.isNotBlank() }?.let { append(it) }
            if (isNotEmpty()) append(" · ")
            horse.color?.takeIf { it.isNotBlank() }?.let { append(it) }
        }
        if (subtitle.isNotBlank()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/* ------- Preview di sicurezza (facoltativa) ------- */

private val demoHorses = listOf(
    Horse(id = 1, ownerId = 10, name = "Starlight", breed = "Sella Italiano", color = "Baio", notes = null),
    Horse(id = 2, ownerId = 10, name = "Vortex", breed = "PSI", color = "Sauro", notes = "Salto ostacoli")
)

@Preview(showBackground = true)
@Composable
private fun HorsesScreenPreview() {
    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            SmallTopAppBar(title = { Text("Cavalli di Rossi") })
            LazyColumn(Modifier.padding(16.dp)) {
                items(demoHorses) { h ->
                    HorseRow(horse = h, onClick = {})
                    Divider()
                }
            }
        }
    }
}
