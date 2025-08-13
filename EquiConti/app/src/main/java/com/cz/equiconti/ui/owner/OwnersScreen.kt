package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

@Composable
fun OwnersScreen(
    navController: NavController,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owners by vm.owners.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Proprietari") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Nuovo") },
                onClick = { navController.navigate("owner/add") }
            )
        }
    ) { pad ->
        if (owners.isEmpty()) {
            Box(Modifier.padding(pad).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nessun proprietario ancora.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(pad).fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(owners, key = { it.id }) { owner ->
                    OwnerRow(owner) { navController.navigate("owner/${owner.id}") }
                }
            }
        }
    }
}

@Composable
private fun OwnerRow(owner: Owner, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = "${owner.lastName} ${owner.firstName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            owner.phone?.let {
                Spacer(Modifier.height(2.dp))
                Text(text = it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
