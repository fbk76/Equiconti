package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cz.equiconti.data.Owner

@Composable
fun OwnersScreen(
    owners: List<Owner>,
    onOwnerClick: (Owner) -> Unit,
    onAddOwner: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddOwner) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Aggiungi proprietario")
            }
        }
    ) { inner ->
        if (owners.isEmpty()) {
            EmptyOwners(modifier = modifier.padding(inner))
        } else {
            OwnersList(
                owners = owners,
                onOwnerClick = onOwnerClick,
                modifier = modifier.padding(inner)
            )
        }
    }
}

@Composable
private fun OwnersList(
    owners: List<Owner>,
    onOwnerClick: (Owner) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(owners, key = { it.id }) { owner ->
            OwnerRow(
                owner = owner,
                onClick = { onOwnerClick(owner) },
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
        }
    }
}

@Composable
private fun OwnerRow(
    owner: Owner,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = listOfNotNull(owner.firstName?.trim(), owner.lastName?.trim())
                .filter { it.isNotEmpty() }
                .joinToString(" "),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun EmptyOwners(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Nessun proprietario.\nTocca + per aggiungerne uno.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
