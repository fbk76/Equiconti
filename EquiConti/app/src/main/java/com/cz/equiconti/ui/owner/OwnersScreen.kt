package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Schermata lista proprietari (versione minima che compila).
 * Puoi sostituire l’elenco fittizio con i tuoi dati reali.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnersScreen(
    onOwnerClick: (ownerId: Long) -> Unit,
    onAddOwner: () -> Unit
) {
    val fakeOwners = listOf(
        1L to "Mario Rossi",
        2L to "Luca Bianchi",
        3L to "Anna Verdi"
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Proprietari") }) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Button(
                onClick = onAddOwner,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
            ) { Text("Aggiungi") }

            LazyColumn(Modifier.fillMaxSize()) {
                items(fakeOwners) { (id, name) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOwnerClick(id) }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(text = name)
                    }
                }
            }
        }
    }
}
