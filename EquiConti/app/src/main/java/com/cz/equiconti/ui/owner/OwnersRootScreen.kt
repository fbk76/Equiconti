package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnersRootScreen(innerPadding: PaddingValues) {
    // Se hai già una schermata "OwnersScreen(...)" usala al posto del contenuto placeholder:
    // OwnersScreen(onOwnerClick = { ... }, onAddOwner = { ... })

    Scaffold(
        topBar = { TopAppBar(title = { Text("Proprietari") }) }
    ) { contentPadding ->
        // placeholder minimale, rimpiazzalo con la tua UI reale
        Text(
            "Lista proprietari (collega qui la tua OwnersScreen)",
            modifier = Modifier
                .padding(innerPadding)
                .padding(contentPadding)
                .fillMaxSize()
        )
    }
}
