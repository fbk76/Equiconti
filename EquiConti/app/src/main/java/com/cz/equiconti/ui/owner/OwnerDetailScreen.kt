package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner
import kotlinx.coroutines.launch

@Composable
fun OwnerDetailScreen(
    nav: NavController,
    ownerId: Long,
    onEdit: (Long) -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    var owner by remember { mutableStateOf<Owner?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(ownerId) {
        owner = vm.getOwnerById(ownerId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Dettaglio proprietario") }) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            owner?.let { o ->
                Text("Nome: ${o.firstName} ${o.lastName}", style = MaterialTheme.typography.titleLarge)
                if (!o.phone.isNullOrBlank()) Text("Telefono: ${o.phone}")

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { onEdit(o.id) }) { Text("Modifica") }
                    OutlinedButton(onClick = {
                        scope.launch {
                            vm.deleteOwner(o)
                            nav.popBackStack()
                        }
                    }) { Text("Elimina") }
                }
            } ?: Text("Caricamento…")
        }
    }
}
