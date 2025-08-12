package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

@Composable
fun OwnerDetailScreen(
    ownerId: Long,
    nav: NavController,
    vm: OwnersViewModel = hiltViewModel()
) {
    // osserva il proprietario
    val owner by vm.ownerFlow(ownerId).collectAsState(initial = null)

    // campi editabili (si riempiono quando arriva l'owner)
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

    val canSave = firstName.isNotBlank() && lastName.isNotBlank()

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Dettagli proprietario") })
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (owner == null) {
                Text("Proprietario non trovato.")
            } else {
                Text(
                    text = "${owner!!.lastName} ${owner!!.firstName}",
                    style = MaterialTheme.typography.titleLarge
                )
                if (!owner!!.phone.isNullOrBlank()) {
                    Text("Tel: ${owner!!.phone}")
                }
            }

            // Pulsanti navigazione funzioni
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { nav.navigate("owner/$ownerId/horses") }) { Text("Cavalli") }
                Button(onClick = { nav.navigate("owner/$ownerId/txn") }) { Text("Movimenti") }
            }

            Divider(Modifier.padding(vertical = 8.dp))

            // Modulo modifica dati
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
                label = { Text("Telefono (opz.)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val toSave = Owner(
                            id = owner?.id ?: ownerId,
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            phone = phone.trim().ifBlank { null }
                        )
                        vm.upsertOwner(toSave)
                        nav.popBackStack()
                    },
                    enabled = canSave
                ) { Text("Salva") }

                Button(
                    onClick = {
                        owner?.let {
                            vm.deleteOwner(it)
                            nav.popBackStack()
                        }
                    },
                    enabled = owner != null
                ) { Text("Elimina") }

                Spacer(Modifier.weight(1f))
                OutlinedButton(onClick = { nav.popBackStack() }) { Text("Indietro") }
            }
        }
    }
}
