package com.cz.equiconti.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier

@Composable
fun ReportScreen(nav: NavController, ownerId: Long) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Report") }) }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            Text("Report in sviluppo (ownerId = $ownerId)")
        }
    }
}
