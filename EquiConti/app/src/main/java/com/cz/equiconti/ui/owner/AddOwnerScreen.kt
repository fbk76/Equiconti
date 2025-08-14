package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner

@Composable
fun AddOwnerScreen(
    onBack: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuovo proprietario") }) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(firstName, { firstName = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(lastName, { lastName = it }, label = { Text("Cognome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(phone, { phone = it }, label = { Text("Telefono") }, modifier = Modifier.fillMaxWidth())

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onBack) { Text("Annulla") }
                Button(
                    onClick = {
                        val o = Owner(
                            id = 0L,
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            phone = phone.trim().ifBlank { null }
                        )
                        vm.upsertOwner(o)
                        onBack()
                    },
                    enabled = lastName.isNotBlank() || firstName.isNotBlank()
                ) { Text("Salva") }
            }
        }
    }
}
