package com.cz.equiconti.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Txn
import com.cz.equiconti.util.formatCurrency
import com.cz.equiconti.vm.TxnVm
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TxnScreen(
    nav: NavController,
    ownerId: Long,
    vm: TxnVm = hiltViewModel()
) {
    LaunchedEffect(ownerId) { vm.load(ownerId) }
    val txns by vm.txns.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Movimenti") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) { Text("+") }
        }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            LazyColumn {
                items(txns) { txn ->
                    ListItem(
                        headlineText = { Text(formatCurrency(txn.amountCents)) },
                        supportingText = {
                            Text(
                                LocalDate.ofInstant(
                                    Instant.ofEpochMilli(txn.date),
                                    ZoneId.systemDefault()
                                ).toString()
                            )
                        }
                    )
                    Divider()
                }
            }
        }

        if (showAdd) {
            AddTxnDialog(
                ownerId = ownerId,
                onDismiss = { showAdd = false },
                onSave = { amountCents, dateMillis, note ->
                    vm.addTxn(
                        Txn(
                            id = 0L,
                            ownerId = ownerId,
                            horseId = null, // opzionale, qui non filtriamo per cavallo
                            amountCents = amountCents,
                            date = dateMillis,
                            note = note
                        )
                    )
                    showAdd = false
                }
            )
        }
    }
}

@Composable
fun AddTxnDialog(
    ownerId: Long,
    onDismiss: () -> Unit,
    onSave: (amountCents: Long, dateMillis: Long, note: String?) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    val todayMillis = remember { System.currentTimeMillis() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aggiungi movimento") },
        text = {
            Column {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = {
