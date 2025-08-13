package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// IMPORT per tastiera (risolvono gli errori del log)
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    onAddTxn: (amount: Double, note: String) -> Unit
) {
    val (amountText, setAmountText) = remember { mutableStateOf("") }
    val (note, setNote) = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti") }
            )
        },
        floatingActionButton = {
            // FAB minimale senza icone (compat)
            FloatingActionButton(
                onClick = {
                    val value = amountText.toDoubleOrNull() ?: 0.0
                    onAddTxn(value, note)
                    setAmountText("")
                    setNote("")
                }
            ) { Text("+") }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = amountText,
                onValueChange = setAmountText,
                label = { Text("Importo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = note,
                onValueChange = setNote,
                label = { Text("Nota") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
