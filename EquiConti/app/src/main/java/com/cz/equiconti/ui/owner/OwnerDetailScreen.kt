package com.cz.equiconti.ui.owner

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun OwnerDetailScreen(
    onBack: () -> Unit,
    onAddHorse: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettaglio proprietario") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Indietro") } }
            )
        }
    ) { _ ->
        // TODO: contenuti reali
        Text("Schermata dettaglio (da completare)")
    }
}
