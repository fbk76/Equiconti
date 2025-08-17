package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    vm: TxnViewModel = hiltViewModel()
) {
    var horseIdText by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    fun doSave() {
        val horseId = horseIdText.toLongOrNull()
        val amount = amountText.toLongOrNull()
        if (horseId != null && amount != null) {
            vm.addTxn(horseId = horseId, amountCents = amount, notes = notes.ifBlank { null })
            onSaved()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo movimento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(
                        onClick = ::doSave,
                        enabled = horseIdText.isNotBlank() && amountText.isNotBlank()
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = "Salva")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = horseIdText,
                onValueChange = { horseIdText = it },
                label = { Text("ID cavallo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Importo (cent)") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Note (opzionale)") },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        }
    }
}
