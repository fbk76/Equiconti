package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Txn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(onBack: () -> Unit, vm: OwnerDetailViewModel = hiltViewModel()) {
    val owner = vm.owner.collectAsState().value
    val horses = vm.horses.collectAsState().value
    val txns = vm.txns.collectAsState().value
    var showHorseDialog = remember { mutableStateOf(false) }
    var showTxnDialog = remember { mutableStateOf(false) }
    var horseName = remember { mutableStateOf(TextFieldValue("")) }
    var txnAmount = remember { mutableStateOf(TextFieldValue("")) }
    var txnNote = remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(owner?.name ?: "Dettaglio") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro") } }) },
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = { showHorseDialog.value = true }, modifier = Modifier.padding(bottom = 16.dp)) { Icon(Icons.Filled.Add, contentDescription = "Nuovo cavallo") }
                FloatingActionButton(onClick = { showTxnDialog.value = true }) { Icon(Icons.Filled.Add, contentDescription = "Nuova transazione") }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            Text("Cavalli", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            if (horses.isEmpty()) { Text("Nessun cavallo per questo proprietario.") }
            else {
                LazyColumn { items(horses) { h -> Text("- ${h.name}", Modifier.padding(vertical = 6.dp)) } }
            }

            Spacer(Modifier.height(16.dp))
            Text("Transazioni", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            if (txns.isEmpty()) { Text("Nessuna transazione.") }
            else {
                LazyColumn {
                    items(txns) { txn ->
                        Text("Importo: ${txn.amount} € - Nota: ${txn.note ?: "Nessuna"}", Modifier.padding(vertical = 6.dp))
                    }
                }
            }
        }
    }

    if (showHorseDialog.value) {
        AlertDialog(
            onDismissRequest = { showHorseDialog.value = false },
            title = { Text("Nuovo cavallo") },
            text = { OutlinedTextField(value = horseName.value, onValueChange = { horseName.value = it }, label = { Text("Nome") }) },
            confirmButton = { TextButton(onClick = { val n = horseName.value.text.trim(); if (n.isNotEmpty()) { vm.addHorse(n); showHorseDialog.value = false; horseName.value = TextFieldValue("") } }) { Text("Salva") } },
            dismissButton = { TextButton(onClick = { showHorseDialog.value = false }) { Text("Annulla") } }
        )
    }

    if (showTxnDialog.value) {
        AlertDialog(
            onDismissRequest = { showTxnDialog.value = false },
            title = { Text("Nuova transazione") },
            text = {
                Column {
                    OutlinedTextField(value = txnAmount.value, onValueChange = { txnAmount.value = it }, label = { Text("Importo") })
                    OutlinedTextField(value = txnNote.value, onValueChange = { txnNote.value = it }, label = { Text("Nota (opzionale)") })
                }
            },
            confirmButton = { TextButton(onClick = {
                val amountStr = txnAmount.value.text.trim()
                val note = txnNote.value.text.trim().ifEmpty { null }
                val amount = amountStr.toDoubleOrNull()
                if (amount != null) {
                    vm.addTxn(amount, note)
                    showTxnDialog.value = false
                    txnAmount.value = TextFieldValue("")
                    txnNote.value = TextFieldValue("")
                }
            }) { Text("Salva") } },
            dismissButton = { TextButton(onClick = { showTxnDialog.value = false }) { Text("Annulla") } }
        )
    }
}
