package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Txn
import com.cz.equiconti.ui.owner.OwnersViewModel
import java.util.*

@Composable
fun TxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    var description by remember { mutableStateOf("") }
    var income by remember { mutableStateOf("") }
    var expense by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Movimento")

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descrizione") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = income,
            onValueChange = { income = it.filter { ch -> ch.isDigit() } },
            label = { Text("Entrata (€)") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = expense,
            onValueChange = { expense = it.filter { ch -> ch.isDigit() } },
            label = { Text("Uscita (€)") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onBack) { Text("Indietro") }
            Button(
                onClick = {
                    // NB: qui trasformo euro interi in centesimi; adatta se usi decimali
                    val incomeCents = income.toLongOrNull()?.times(100) ?: 0L
                    val expenseCents = expense.toLongOrNull()?.times(100) ?: 0L

                    val txn = Txn(
                        txnId = 0L, // autoGenerate
                        ownerId = ownerId,
                        dateMillis = System.currentTimeMillis(),
                        description = if (description.isBlank()) null else description,
                        incomeCents = incomeCents,
                        expenseCents = expenseCents
                    )
                    vm.addTxn(txn)
                    onBack()
                }
            ) { Text("Salva") }
        }
    }
}
