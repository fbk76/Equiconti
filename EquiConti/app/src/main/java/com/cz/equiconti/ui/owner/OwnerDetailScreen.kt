package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onAddHorse: () -> Unit,
    onOpenTxns: () -> Unit,
    vm: OwnerDetailViewModel = hiltViewModel()
) {
    val owner by vm.owner(ownerId).collectAsState(initial = null)
    val horses by vm.horses(ownerId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(owner?.name ?: "Proprietario #$ownerId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    TextButton(onClick = onOpenTxns) { Text("Movimenti") }
                    IconButton(onClick = onAddHorse) {
                        Icon(Icons.Filled.Add, contentDescription = "Aggiungi cavallo")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Dettagli proprietario")
            Spacer(Modifier.height(12.dp))
            Text("Cavalli:")
            Spacer(Modifier.height(8.dp))

            LazyColumn {
                items(horses, key = { it.id }) { h ->
                    Text("• ${h.name}")
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
    }
}
