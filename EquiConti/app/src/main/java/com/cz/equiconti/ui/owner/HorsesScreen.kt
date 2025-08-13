package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorsesScreen(
    onAddHorse: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Cavalli") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHorse) {
                Icon(Icons.Filled.Add, contentDescription = "Nuovo cavallo")
            }
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            content()
        }
    }
}
