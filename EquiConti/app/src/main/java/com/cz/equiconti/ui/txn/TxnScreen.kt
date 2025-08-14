package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Txn
import com.cz.equiconti.ui.owner.OwnersViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@Composable
fun TxnScreen(
    ownerId: Long,
    nav: NavController? = null,
    onBack: (() -> Unit)? = null,
    vm: OwnersViewModel = hiltViewModel()
) {
    val txns by vm.txns(ownerId).collectAsState(initial = emptyList())

    // Stati del mini-form (non collegato a salvataggio finché non ci dai la firma di insert)
    var amount by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti") },
                navigationIcon = {
                    IconButton(
                        onClick = { onBack?.invoke() ?: nav?.popBackStack() }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Qui collegheremo il salvataggio quando ci dai la firma Repo/VM (es. vm.addTxn(...))
                    // intanto lasciamo solo la pulizia dei campi per non rompere nulla
                    amount = ""
                    note = ""
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nuovo movimento")
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            // Mini form (solo UI per ora)
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Importo") },
                        placeholder = { Text("0.00") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Nota") },
                        keyboardOptions = KeyboardOptions.Default,
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        // Quando ci dai la funzione di salvataggio, attiviamo questo bottone
                        // e invochiamo il metodo del ViewModel/Repo
                        Button(
                            onClick = { /* TODO salva txn */ },
                            enabled = false // per ora disabilitato: evitiamo errori di compilazione
                        ) {
                            Text("Aggiungi")
                        }
                    }
                }
            }

            // Lista movimenti
            if (txns.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nessun movimento presente.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(txns) { t ->
                        TxnRow(txn = t)
                    }
                }
            }
        }
    }
}

@Composable
private fun TxnRow(txn: Txn) {
    // Mostriamo le info base; adatta i campi alla tua data class Txn
    // (se i nomi fossero diversi, questa parte è l’unica da ritoccare)
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = "ID: ${txn.id}", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            // Prova a mostrare genericamente toString per non fare assunzioni sui campi
            Text(text = txn.toString(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
