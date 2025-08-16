@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.cz.equiconti.ui.report

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ReportScreen(
    onGeneratePdf: (Context) -> Unit,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Report") })
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {
            Text(
                "Genera il PDF dei movimenti per il periodo selezionato.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = { onGeneratePdf(ctx) }) { Text("Genera PDF") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onBack) { Text("Indietro") }
        }
    }
}
