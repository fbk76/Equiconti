package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner
import kotlinx.coroutines.launch

@Composable
fun AddOwnerScreen(
    nav: NavController,
    ownerId: Long? = null,             // se in futuro vorrai “modifica”
    onSaved: (Long) -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    var first by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // se passi un id per edit, puoi caricare i dati
    LaunchedEffect(ownerId) {
        if (ownerId != null && ownerId != 0L) {
            vm.getOwnerById(ownerId)?.let {
                first = it.firstName
                last = it.lastName
                phone = it.phone.orEmpty()
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuovo proprietario") }) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(first, { first = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(last, { last = it }, label = { Text("Cognome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(phone, { phone = it }, label = { Text("Telefono (opz.)") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    scope.launch {
                        val id = vm.upsertOwner(
                            Owner(
                                id = ownerId ?: 0L,
                                firstName = first.trim(),
                                lastName = last.trim(),
                                phone = phone.ifBlank { null }
                            )
                        )
                        onSaved(id)
                    }
                },
                enabled = first.isNotBlank() && last.isNotBlank()
            ) { Text("Salva") }
        }
    }
}
