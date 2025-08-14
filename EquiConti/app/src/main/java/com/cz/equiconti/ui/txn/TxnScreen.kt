package com.cz.equiconti.ui.txn

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate

/**
 * Schermata per inserire un movimento (entrata/uscita).
 *
 * @param ownerId   id del proprietario a cui agganciare il movimento (mostrato solo per completezza)
 * @param onBack    callback quando si torna indietro (annulla)
 * @param onSave    callback con i valori digitati. Collega questa lambda al tuo ViewModel/Repo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onSave: (amount: Double, isIncome: Boolean, date: LocalDate, note: String) -> Unit
) {
    val ctx = LocalContext.current

    var amountText by rememberSaveable { mutableStateOf("") }
    var isIncome by rememberSaveable { mutableStateOf(true) }
    var dateText by rememberSaveable { mutableStateOf(LocalDate.now().toString()) }
    var note by rememberSaveable { mutableStateOf("") }

    val scroll = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo movimento") }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) { Text("Annulla") }

                Button(
                    onClick = {
                        val amount = amountText.replace(',', '.').toDoubleOrNull()
                        val parsedDate = runCatching { LocalDate.parse(dateText) }.getOrNull()

                        when {
                            amount == null || amount <= 0.0 -> {
                                Toast.makeText(ctx, "Importo non valido", Toast.LENGTH_SHORT).show()
                            }
                            parsedDate == null -> {
                                Toast.makeText(ctx, "Data non valida (YYYY-MM-DD)", Toast.LENGTH_SHORT).show()
                            }
                            else -> onSave(amount, isIncome, parsedDate, note.trim())
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Salva") }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Importo
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Importo") },
                placeholder = { Text("es. 120.00") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onNext = { /* focus next */ }),
                modifier = Modifier.fillMaxWidth()
            )

            // Entrata / Uscita
            Text("Tipo movimento")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterChip(
                    selected = isIncome,
                    onClick = { isIncome = true },
                    label = { Text("Entrata") }
                )
                FilterChip(
                    selected = !isIncome,
                    onClick = { isIncome = false },
                    label = { Text("Uscita") }
                )
            }

            // Data
            OutlinedTextField(
                value = dateText,
                onValueChange = { dateText = it },
                label = { Text("Data (YYYY-MM-DD)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Ascii
                ),
                keyboardActions = KeyboardActions(onNext = { /* focus next */ }),
                modifier = Modifier.fillMaxWidth()
            )

            // Note
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (opzionale)") },
                maxLines = 4,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onDone = { /* close */ }),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            )

            // Info proprietario (facoltativa)
            AssistChip(
                onClick = { /* no-op */ },
                label = { Text("Owner ID: $ownerId") }
            )
        }
    }
}
