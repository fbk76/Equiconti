package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

@Composable
fun OwnerDetailScreen(
    nav: NavController,
    owner: Owner
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("${owner.firstName} ${owner.lastName}") }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("Nome: ${owner.firstName}")
            Text("Cognome: ${owner.lastName}")
            Text("Telefono: ${owner.phone}")
        }
    }
}
