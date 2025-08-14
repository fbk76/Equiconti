package com.cz.equiconti.ui.txn

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Txn
import com.cz.equiconti.ui.owner.OwnersViewModel
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    var amount by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(true) }
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) }
    var note by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Nuovo movimento") }) }) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = amount, onValueChange = { amount = it },
                label = { Text("Importo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterChip(selected = isIncome, onClick = { isIncome = true }, label = { Text("Entrata") })
                FilterChip(selected = !isIncome, onClick = { isIncome = false }, label = { Text("Uscita") })
            }

            OutlinedTextField(
                value = dateText, onValueChange = { dateText = it },
                label = { Text("Data (YYYY-MM-DD)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = note, onValueChange = { note = it },
                label = { Text("Causale/Note") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack) { Text("Annulla") }
                Button(
                    onClick = {
                        val amountVal = amount.replace(',', '.').toDoubleOrNull() ?: 0.0
                        val date = runCatching { LocalDate.parse(dateText) }.getOrNull() ?: LocalDate.now()
                        val dateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        val incCents = if (isIncome) (amountVal * 100).toLong() else 0L
                        val expCents = if (!isIncome) (amountVal * 100).toLong() else 0L

                        val txn = Txn(
                            txnId = 0L,
                            ownerId = ownerId,
                            horseId = null,
                            dateMillis = dateMillis,
                            operation = if (note.isNotBlank()) note.trim() else if (isIncome) "Entrata" else "Uscita",
                            incomeCents = incCents,
                            expenseCents = expCents,
                            note = note.trim().ifBlank { null }
                        )
                        vm.upsertTxn(txn)
                        onBack()
                    },
                    enabled = amount.isNotBlank()
                ) { Text("Salva") }
            }
        }
    }
}
