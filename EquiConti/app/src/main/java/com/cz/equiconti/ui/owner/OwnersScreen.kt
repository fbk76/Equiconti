package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

@Composable
fun OwnersScreen(
    nav: NavController,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owners by vm.owners.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Proprietari") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate("owner/add") }) {
                Icon(Icons.Filled.Add, contentDescription = "Aggiungi")
            }
        }
    ) { padding ->
        if (owners.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Nessun proprietario.\nTocca + per aggiungerne uno.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(owners, key = { it.id }) { o ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable { nav.navigate("owner/${o.id}") }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text("${o.firstName} ${o.lastName}".trim(), style = MaterialTheme.typography.titleMedium)
                        if (!o.phone.isNullOrBlank()) {
                            Spacer(Modifier.height(2.dp))
                            Text(o.phone!!, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Divider()
                }
            }
        }
    }
}
