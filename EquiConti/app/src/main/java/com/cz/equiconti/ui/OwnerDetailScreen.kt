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
import com.cz.equiconti.data.Horse
import com.cz.equiconti.vm.OwnerDetailVm
import com.cz.equiconti.util.formatCurrency

@Composable
fun OwnerDetailScreen(nav: NavController, ownerId: Long, vm: OwnerDetailVm = hiltViewModel()) {
    LaunchedEffect(ownerId) { vm.load(ownerId) }
    val owner = vm.owner.collectAsState().value
    val horses = vm.horses.collectAsState().value
    val balance = vm.balance.collectAsState().value
    var showAddHorse by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar("Scheda cliente") { nav.popBackStack() }
        },
        bottomBar = {
            Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { nav.navigate("txns/$ownerId") }) { Text("Movimenti") }
                Button(onClick = { nav.navigate("report/$ownerId") }) { Text("Report") }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddHorse = true }) { Text("+") }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            owner?.let {
                LabeledValue("Cliente", "${it.owner.surname} ${it.owner.name}")
                LabeledValue("Telefono", it.owner.phone ?: "-")
            }
            Text("Cavalli", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(horses) { h ->
                    ListItem(
                        headlineContent = { Text(h.name) },
                        supportingContent = { Text("Quota mensile: " + formatCurrency(h.monthlyFeeCents)) }
                    )
                    Divider()
                }
            }
            Divider()
            Text("Saldo attuale: " + formatCurrency(balance ?: 0), style = MaterialTheme.typography.titleLarge)
        }
    }

    if (showAddHorse) {
        HorseDialog(onDismiss = { showAddHorse = false }, onSave = { vm.addHorse(it); showAddHorse = false }, ownerId = ownerId)
    }
}

@Composable
fun HorseDialog(onDismiss: () -> Unit, onSave: (Horse) -> Unit, ownerId: Long) {
    var name by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val cents = fee.toDoubleOrNull()?.let { (it * 100).toLong() } ?: 0
                    onSave(Horse(ownerId = ownerId, name = name, monthlyFeeCents = cents))
                },
                enabled = name.isNotBlank()
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
        title = { Text("Aggiungi cavallo") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Nome cavallo") }, singleLine = true)
                OutlinedTextField(fee, { fee = it }, label = { Text("Quota mensile (€)") }, singleLine = true)
            }
        }
    )
}
