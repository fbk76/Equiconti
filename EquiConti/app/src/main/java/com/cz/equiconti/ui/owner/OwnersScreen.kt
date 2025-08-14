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

/**
 * Elenco proprietari.
 * Firma compatibile con il tuo NavGraph: accetta NavController e usa OwnersViewModel via Hilt.
 */
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
        OwnersList(
            owners = owners,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            onClick = { ownerId -> nav.navigate("owner/$ownerId") }
        )
    }
}

@Composable
private fun OwnersList(
    owners: List<Owner>,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(owners, key = { it.id }) { o ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .clickable { onClick(o.id) }
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "${o.firstName} ${o.lastName}".trim(),
                    style = MaterialTheme.typography.titleMedium
                )
                if (!o.phone.isNullOrBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = o.phone!!,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Divider()
        }
    }
}
