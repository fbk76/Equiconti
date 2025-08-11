package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun TxnScreen(
    nav: NavHostController,
    ownerId: Long
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Movimenti ownerId=$ownerId (placeholder)", style = MaterialTheme.typography.titleLarge)
            Button(onClick = { nav.popBackStack() }) { Text("Indietro") }
        }
    }
}
