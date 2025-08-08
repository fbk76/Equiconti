package com.cz.equiconti.ui

import androidx.compose.foundation.clickable
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

@Composable
fun OwnersScreen(nav: NavController, vm: OwnersVm = hiltViewModel()) {
    val owners by vm.owners.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar("Clienti") },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) { Text("+") }
        }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            LazyColumn {
                items(owners) { o ->
                    ListItem(
                        headlineContent = { Text("${o.surname} ${o.name}") },
                        supportingContent = { Text(o.phone ?: "") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { nav.navigate("owner/${o.ownerId}") }
                    )
                    Divider()
                }
            }
        }
    }

    if (showAdd) {
        OwnerDialog(
            onDismiss = { showAdd = false },
            onSave = { vm.addOwner(it); showAdd = false }
        )
    }
}
