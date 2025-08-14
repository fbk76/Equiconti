package com.cz.equiconti.ui.txn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.text.KeyboardOptions
import com.cz.equiconti.data.Txn

/**
 * Schermata dei movimenti (entrate/uscite) del proprietario.
 *
 * @param nav         NavController per la navigazione.
 * @param ownerId     Id del proprietario a cui appartengono i movimenti.
 * @param txns        Lista dei movimenti già presenti (arriva dal ViewModel).
 * @param onAdd       Callback invocata alla pressione di "Salva" nel form:
 *                    (amount: Double, isIncome: Boolean, note: String) -> Unit
 * @param onDelete    Callback per eliminare un movimento esistente.
 * @param onBack      Callback del tasto "indietro".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxnScreen(
    nav: NavController,
    ownerId: Long,
    txns: List<Txn>,
    onAdd: (amount: Double, isIncome: Boolean, note: String) -> Unit,
    onDelete: (Txn) -> Unit,
    onBack: () -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var amountText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var typeIndex by remember { mutableIntStateOf(0) } // 0 = Entrata, 1 = Uscita

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movimenti") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showForm = !showForm }) {
                Icon(
                    if (showForm) Icons.Filled.Done else Icons.Filled.Edit,
                    contentDescription = if (showForm) "Chiudi" else "Nuovo movimento"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Form di inserimento
            if (showForm) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Nuovo movimento",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(Modifier.height(12.dp))

                        SingleChoiceSegmentedButtonRow {
                            SegmentedButton(
                                selected = typeIndex == 0,
                                onClick = { typeIndex = 0 },
                                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                            ) { Text("Entrata") }

                            SegmentedButton(
                                selected = typeIndex == 1,
                                onClick = { typeIndex = 1 },
                                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                            ) { Text("Uscita") }
                        }

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = amountText,
                            onValueChange = { amountText = it },
                            label = { Text("Importo") },
                            placeholder = { Text("es. 50.00") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Note") },
                            placeholder = { Text("descrizione...") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = {
                                    val amount = amountText.replace(',', '.').toDoubleOrNull()
                                    if (amount != null && amount > 0.0) {
                                        onAdd(amount, typeIndex == 0, note.trim())
                                        // reset campi
                                        amountText = ""
                                        note = ""
                                        typeIndex = 0
                                        showForm = false
                                    }
                                }
                            ) { Text("Salva") }

                            Button(onClick = {
                                amountText = ""
                                note = ""
                                typeIndex = 0
                                showForm = false
                            }) { Text("Annulla") }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            // Lista movimenti
            if (txns.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(txns) { t ->
                        TxnRow(
                            txn = t,
                            onDelete = { onDelete(t) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Rappresentazione “neutra” di una riga movimento.
 * Non assumiamo dettagli del modello Txn: usiamo le sue proprietà in modo conservativo.
 * Se il tuo Txn ha campi (amount, isIncome, note, date…), puoi adattare il bind qui.
 */
@Composable
private fun TxnRow(
    txn: Txn,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // “Pallino” colorato a sinistra (verde/rosso se riconosciamo entrata/uscita dal testo)
            val text = txn.toString().lowercase()
            val income = text.contains("entrata") || text.contains("+")
            val dotColor = if (income) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

            Spacer(
                modifier = Modifier
                    .height(32.dp)
                    .background(dotColor, CircleShape)
                    .clip(CircleShape)
                    .padding(0.dp)
                    .fillMaxWidth(0f) // trucco per avere solo il cerchietto
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                // Titolo/descrizione: proviamo a mostrare qualcosa di utile
                Text(
                    text = txn.toString(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Elimina")
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Nessun movimento",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Aggiungi una entrata o uscita con il pulsante in basso a destra.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
