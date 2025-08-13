package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Txn
import com.cz.equiconti.ui.owner.OwnersViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val txns = vm.txns(ownerId).collectAsState(initial = emptyList()).value
    val balance = txns.sumOf { it.incomeCents - it.expenseCents }
    val nf = remember { NumberFormat.getCurrencyInstance() }

    var operation by remember { mutableStateOf("") }
    var income by remember { mutableStateOf("") }
    var expense by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Movimenti", style = MaterialTheme.typography.headlineSmall)
        Text("Saldo: ${nf.format(balance / 100.0)}")

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(txns, key = { it.txnId }) { t -> TxnRow(t); Divider() }
        }

        OutlinedTextField(value = operation, onValueChange = { operation = it }, label = { Text("Operazione/Descrizione") }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = income, onValueChange = { income = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
                label = { Text("Entrata (€)") }, keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = expense, onValueChange = { expense = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
                label = { Text("Uscita (€)") }, keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onBack) { Text("Indietro") }
            Button(
                enabled = operation.isNotBlank(),
                onClick = {
                    val inc = ((income.replace(',', '.').toDoubleOrNull() ?: 0.0) * 100).toLong()
                    val exp = ((expense.replace(',', '.').toDoubleOrNull() ?: 0.0) * 100).toLong()
                    vm.addTxn(
                        Txn(
                            txnId = 0L,
                            ownerId = ownerId,
                            horseId = null,
                            dateMillis = System.currentTimeMillis(),
                            operation = operation.trim(),
                            incomeCents = inc,
                            expenseCents = exp,
                            note = null
                        )
                    )
                    operation = ""; income = ""; expense = ""
                }
            ) { Text("Salva") }
        }
    }
}

@Composable
private fun TxnRow(t: Txn) {
    val nf = remember { NumberFormat.getCurrencyInstance() }
    val df = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val amount = (t.incomeCents - t.expenseCents) / 100.0
    Column {
        Text(df.format(Date(t.dateMillis)), style = MaterialTheme.typography.labelMedium)
        Text(t.operation, style = MaterialTheme.typography.titleMedium)
        Text(nf.format(amount), style = MaterialTheme.typography.bodyLarge)
    }
}
