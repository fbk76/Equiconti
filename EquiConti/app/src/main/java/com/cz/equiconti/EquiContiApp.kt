package com.cz.equiconti
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation   // <-- questa abilita la funzione navigation{}

import androidx.hilt.navigation.compose.hiltViewModel  // <-- per hiltViewModel()

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cz.equiconti.data.Owner
import com.cz.equiconti.ui.OwnerDetailViewModel
import com.cz.equiconti.ui.OwnersViewModel

@Composable
fun EquiContiApp() {
    val nav = rememberNavController()
    MaterialTheme {
        Surface {
            NavHost(navController = nav, startDestination = "owners") {

                composable("owners") {
                    OwnersScreen(
                        onOpenOwner = { ownerId -> nav.navigate("owner/$ownerId") }
                    )
                }

                composable(
                    route = "owner/{ownerId}",
                    arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
                ) {
                    OwnerDetailScreen(onBack = { nav.popBackStack() })
                }
            }
        }
    }
}

// ---------------- Owners ----------------

@Composable
private fun OwnersScreen(
    onOpenOwner: (Long) -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val owners by vm.owners.collectAsState()

    Scaffold(
        floatingActionButton = {
            var open by remember { mutableStateOf(false) }
            FloatingActionButton(onClick = { open = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Nuovo proprietario")
            }
            if (open) {
                AddTextDialog(
                    title = "Nuovo proprietario",
                    label = "Nome",
                    onDismiss = { open = false },
                    onConfirm = { name ->
                        vm.addOwner(name)
                        open = false
                    }
                )
            }
        },
        topBar = { TopAppBar(title = { Text("Proprietari") }) }
    ) { padding ->
        if (owners.isEmpty()) {
            Box(Modifier
                .fillMaxSize()
                .padding(padding),
                contentAlignment = Alignment.Center
            ) { Text("Nessun proprietario. Premi + per aggiungerne uno.") }
        } else {
            LazyColumn(Modifier.padding(padding)) {
                items(owners) { owner ->
                    ListItem(
                        headlineContent = { Text(owner.name) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        overlineContent = { Text("ID: ${owner.id}") },
                        supportingContent = { Text("Tocca per aprire") },
                        tonalElevation = 2.dp,
                        shadowElevation = 1.dp,
                        leadingContent = { }
                    )
                    Divider()
                    // Item click area
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    // Navigate when clicking the ListItem
                    LaunchedEffect(owner.id) {
                        // no-op (purely UI item)
                    }
                    // Wrap with clickable:
                    // Simpler: we use ListItem + clickable wrapper
                }
            }
            // Quick clickable overlay: easier to keep code short — or make ListItem clickable:
            // Let's provide a simple column again with clickable Text:
            LazyColumn(Modifier.padding(padding)) {
                items(owners) { o ->
                    TextButton(
                        onClick = { onOpenOwner(o.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(o.name, modifier = Modifier.padding(16.dp))
                    }
                    Divider()
                }
            }
        }
    }
}

// ---------------- Owner detail ----------------

@Composable
private fun OwnerDetailScreen(
    onBack: () -> Unit,
    vm: OwnerDetailViewModel = hiltViewModel()
) {
    val owner by vm.owner.collectAsState()
    val horses by vm.horses.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(owner?.name ?: "Dettaglio") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        },
        floatingActionButton = {
            var open by remember { mutableStateOf(false) }
            FloatingActionButton(onClick = { open = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Nuovo cavallo")
            }
            if (open) {
                AddTextDialog(
                    title = "Nuovo cavallo",
                    label = "Nome",
                    onDismiss = { open = false },
                    onConfirm = { name ->
                        vm.addHorse(name)
                        open = false
                    }
                )
            }
        }
    ) { padding ->
        Column(Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {
            Text("Cavalli", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            if (horses.isEmpty()) {
                Text("Nessun cavallo per questo proprietario.")
            } else {
                LazyColumn {
                    items(horses) { h ->
                        Text("- ${h.name}", Modifier.padding(vertical = 6.dp))
                    }
                }
            }
        }
    }
}

// ---------------- Shared dialog ----------------

@Composable
private fun AddTextDialog(
    title: String,
    label: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember { mutableStateOf(TextFieldValue("")) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text(label) }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(value.text.trim()) },
                enabled = value.text.isNotBlank()
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } }
    )
}
