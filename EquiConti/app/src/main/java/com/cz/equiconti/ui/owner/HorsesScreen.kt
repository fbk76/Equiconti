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
    val owner by vm.ownerFlow(ownerId).collectAsState(initial = null)

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

    Scaffold(topBar = { SmallTopAppBar(title = { Text("Dettagli proprietario") }) }) { pad ->
        Column(
            Modifier.padding(pad).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (owner == null) {
                Text("Proprietario non trovato.")
            } else {
                Text("${owner!!.lastName} ${owner!!.firstName}", style = MaterialTheme.typography.titleLarge)
                if (!owner!!.phone.isNullOrBlank()) Text("Tel: ${owner!!.phone}")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { nav.navigate("owner/$ownerId/horses") }) { Text("Cavalli") }
                Button(onClick = { nav.navigate("owner/$ownerId/txns") }) { Text("Movimenti") }
            }

            Divider(Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = firstName, onValueChange = { firstName = it },
                label = { Text("Nome") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
            OutlinedTextField(
                value = lastName, onValueChange = { lastName = it },
                label = { Text("Cognome") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                label = { Text("Telefono (opz.)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )

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
                    enabled = firstName.isNotBlank() && lastName.isNotBlank()
                ) { Text("Salva") }

                Button(
                    onClick = {
                        owner?.let { vm.deleteOwner(it); nav.popBackStack() }
                    },
                    enabled = owner != null
                ) { Text("Elimina") }

                Spacer(Modifier.weight(1f))
                OutlinedButton(onClick = { nav.popBackStack() }) { Text("Indietro") }
            }
        }
    }
}
