package com.cz.equiconti.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun OwnerDetailScreen(nav: NavController, ownerId: Long) {
    // Versione neutra per compilare: niente addHorse
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Dettaglio proprietario") }) }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            Text("Owner #$ownerId")
            Text("Schermata in lavorazione.")
        }
    }
}
