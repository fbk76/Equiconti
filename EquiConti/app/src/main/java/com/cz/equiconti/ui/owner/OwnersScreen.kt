package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

@Composable
fun OwnersScreen(
    nav: NavController,
    onOwnerClick: (Long) -> Unit,
    onAddOwner: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owners by vm.owners.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddOwner) { Text("+") }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Proprietari", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            owners.forEach { o ->
                OwnerRow(o, onClick = { onOwnerClick(o.id) })
                Divider()
            }
        }
    }
}

@Composable
private fun OwnerRow(o: Owner, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp)
    ) {
        Text("${o.firstName} ${o.lastName}")
    }
}
