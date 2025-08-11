package com.cz.equiconti.ui.owner

import androidx.compose.runtime.Composable
import com.cz.equiconti.data.Owner

@Composable
fun OwnersScreen(
    state: OwnersUiState,                 // oppure: owners: List<Owner>
    onOwnerClick: (Long) -> Unit,
    onAddOwner: () -> Unit
) {
    // Esempio d’uso:
    // LazyColumn {
    //   items(state.items) { owner ->
    //       OwnerRow(owner = owner, onClick = { onOwnerClick(owner.id) })
    //   }
    // }
    // FloatingActionButton(onClick = onAddOwner) { Icon(Icons.Default.Add, null) }
}
