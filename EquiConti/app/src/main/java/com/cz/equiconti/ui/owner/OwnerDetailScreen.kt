package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * Dettaglio proprietario: app bar con back, azione "Aggiungi cavallo"
 * e pulsante per aprire i movimenti del proprietario.
 *
 * @param ownerId     id del proprietario
 * @param onBack      callback per tornare indietro
 * @param onOpenTxns  naviga alla lista movimenti del proprietario
 * @param nav         NavController per navigare alla schermata di aggiunta cavallo
 */
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onOpenTxns: () -> Unit,
    nav: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettagli proprietario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                },
                actions = {
                    // Aggiungi cavallo
                    IconButton(onClick = { nav.navigate("owner/$ownerId/addHorse") }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Aggiungi cavallo"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Qui puoi mostrare i dettagli reali del proprietario e la lista cavalli
            Text(
                text = "Proprietario #$ownerId",
                style = MaterialTheme.typography.titleLarge
            )

            // Pulsante/testo tappabile per aprire i movimenti
            // (sostituisci con un vero Button se preferisci)
            Text(
                text = "Vedi movimenti",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 4.dp)
                    .then(Modifier) // placeholder per eventuali clickable
            )

            // Esempio: chiama onOpenTxns quando vuoi aprire i movimenti
            // Se preferisci un vero bottone:
            // Button(onClick = onOpenTxns) { Text("Vedi movimenti") }
        }
    }
}
