package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner
import kotlinx.coroutines.launch

@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    nav: NavController,
    vm: OwnersViewModel = hiltViewModel()
) {
    // osserva il proprietario (può essere null se non trovato)
    val owner by vm.ownerFlow(ownerId).collectAsState(initial = null)

    // stato dei campi editabili (inizializzati dal proprietario quando arriva)
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName  by rememberSaveable { mutableStateOf("") }
    var phone     by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(owner?.id) {
        owner?.let {
            firstName = it.firstName
            lastName  = it.lastName
            phone     = it.phone.orEmpty()
        }
    }

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Dettagli")
            // Semplice "Indietro" testuale per evitare dipendenze icone
            Button(onClick = { nav.popBackStack() }) { Text("Indietro") }
        }

        if (owner == null) {
            Text(text = "Proprietario non trovato.")
            Spacer(Modifier.height(12.dp))
            // Permetto comunque l’editing per sicurezza (es. se ownerId=0 in futuro)
        }

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Nome") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            )
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Cognome") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            )
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Telefono (opzionale)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp
