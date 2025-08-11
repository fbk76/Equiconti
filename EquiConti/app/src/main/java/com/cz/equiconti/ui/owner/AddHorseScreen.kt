package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Schermata semplice per aggiungere un cavallo.
 * Per ora il tasto "Salva" torna indietro senza fare nulla (stub),
 * va bene solo per far compilare e proseguire.
 */
@Composable
fun AddHorseScreen(
    ownerId: Long,
    onBack: () -> Unit
) {
    val (name, setName) = remember { mutableStateOf("") }
    val (monthlyFee, setMonthlyFee) = remember { mutableStateOf("") }
    val (notes, setNotes) = remember { mutableStateOf("") }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Nuovo cavallo") }) }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("Owner ID: $ownerId")

            OutlinedTextField(
                value = name,
                onValueChange = setName,
                label = { Text("Nome cavallo") }
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = monthlyFee,
                onValueChange = setMonthlyFee,
                label = { Text("Quota mensile (€)") }
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = setNotes,
                label = { Text("Note (opzionale)") }
            )
            Spacer(Modifier.height(16.dp))

            Button(onClick = { onBack() }) {
                Text("Salva")
            }
            TextButton(onClick = onBack) {
                Text("Annulla")
            }
        }
    }
}
