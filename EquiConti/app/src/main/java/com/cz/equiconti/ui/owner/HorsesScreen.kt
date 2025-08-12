package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.ui.owner.OwnersViewModel
import kotlinx.coroutines.launch

/**
 * Lista cavalli per un proprietario.
 *
 * - Mostra il nome del proprietario nella top bar
 * - Elenca i cavalli (se vuoto -> messaggio “nessun cavallo”)
 * - FAB: chiama onAddHorse(ownerId)
 */
@Composable
fun HorsesScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: (Long) -> Unit,
    onHorseClick: (Horse) -> Unit = {},
    vm: OwnersViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    // carico l'owner per il titolo
    var owner by remember { mutableStateOf<Owner?>(null) }
    LaunchedEffect(ownerId) {
        owner = vm.getOwnerById(ownerId)
    }

    // flusso cavalli
    val horses by vm.observeHorses(ownerId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        owner?.let { "${it.lastName} ${it.firstName}" } ?: "Cavalli"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddHorse(ownerId) }) {
                Icon(Icons.Filled.Add, contentDescription = "Aggiungi cavallo")
            }
        }
    ) { padding ->
        if (horses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessun cavallo registrato.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(horses) { horse ->
                    HorseRow(
                        horse = horse,
                        onClick = { onHorseClick(horse) }
                    )
                    Divider()
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(horse.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            val subtitle = buildString {
                horse.breed?.let { append(it) }
                if (isNotEmpty()) append("  ")
                horse.color?.let { append(it) }
            }
            if (subtitle.isNotBlank()) {
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
