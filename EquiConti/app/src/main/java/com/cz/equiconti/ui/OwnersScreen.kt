package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.vm.OwnersVm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnersScreen(
    nav: NavController,
    vm: OwnersVm = hiltViewModel()
) {
    val owners by vm.owners.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Clienti") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate("owner/new") }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(owners) { o ->
                ListItem(
                    headlineContent = { Text("${o.surname}  ${o.name}") },
                    supportingContent = { Text(o.phone ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )
                Divider()
            }
        }
    }
}
