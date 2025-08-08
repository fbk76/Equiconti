package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(title: String, onBack: (() -> Unit)? = null) {
    SmallTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (onBack != null) {
                TextButton(onClick = onBack) { Text("Indietro") }
            }
        }
    )
}

@Composable
fun LabeledValue(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
