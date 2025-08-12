package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse

@Composable
fun HorsesScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: (Long) -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val horses = vm.horses(ownerId).collectAsState(initial = emptyList()).value

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Cavalli") }) }
    ) { pad ->
        Column(
            Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onBack) { Text("Indietro") }
                Button(onClick = { onAddHorse(ownerId) }) { Text("Nuovo cavallo") }
            }

            if (horses.isEmpty()) {
                Text("Nessun cavallo registrato.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(horses, key = { it.id }) { h ->
                        HorseRow(h)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun HorseRow(h: Horse) {
    Column {
        Text(h.name, style = MaterialTheme.typography.titleMedium)
        if (!h.notes.isNullOrBlank()) {
            Text(h.notes!!, style = MaterialTheme.typography.bodySmall)
        }
        if (h.monthlyFeeCents > 0) {
            val euro = (h.monthlyFeeCents / 100.0)
            Text("Quota mensile: €%.2f".format(euro), style = MaterialTheme.typography.bodySmall)
        }
    }
}
