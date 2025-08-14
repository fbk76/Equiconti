package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Schermata di inserimento movimento.
 * Non aggancia ancora il DB: espone onSave che puoi collegare al Repo/VM dal NavGraph.
 */
@Composable
fun TxnScreen(
    nav: NavController,
    ownerId: Long,
    onSave: ((amount: Double, description: String, isIncome: Boolean) -> Unit)? = null,
    onBack: () -> Unit = { nav.popBackStack() }
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(true) } // true=Entrata, false=Uscita

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Nuovo movimento", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Importo") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrizione") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { isIncome = true }, enabled = !isIncome) { Text("Entrata") }
                    Button(onClick = { isIncome = false }, enabled = isIncome) { Text("Uscita") }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Indietro") }
            Button(
                onClick = {
                    val amount = amountText.replace(',', '.').toDoubleOrNull() ?: 0.0
                    onSave?.invoke(amount, description.trim(), isIncome)
                    onBack()
                },
                modifier = Modifier.weight(1f)
            ) { Text("Salva") }
        }
    }
}
