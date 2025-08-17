package com.cz.equiconti.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Owner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnersScreen(onOpenOwner: (Long) -> Unit, vm: OwnersViewModel = hiltViewModel()) {
    var showDialog = remember { mutableStateOf(false) }
    var text = remember { mutableStateOf(TextFieldValue("")) }
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Proprietari") }) },
        floatingActionButton = { FloatingActionButton(onClick = { showDialog.value = true }) { Icon(Icons.Filled.Add, contentDescription = "Nuovo proprietario") } }
    ) { padding ->
        val owners = vm.owners.collectAsState().value
        OwnersList(owners = owners, modifier = Modifier.padding(padding).fillMaxSize(), onOpenOwner = onOpenOwner)
    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Nuovo proprietario") },
            text = { OutlinedTextField(value = text.value, onValueChange = { text.value = it }, singleLine = true, label = { Text("Nome") }) },
            confirmButton = { TextButton(onClick = { val name = text.value.text.trim(); if (name.isNotEmpty()) vm.addOwner(name); showDialog.value = false; text.value = TextFieldValue("") }) { Text("Salva") } },
            dismissButton = { TextButton(onClick = { showDialog.value = false }) { Text("Annulla") } }
        )
    }
}

@Composable
private fun OwnersList(owners: List<Owner>, modifier: Modifier = Modifier, onOpenOwner: (Long) -> Unit) {
    if (owners.isEmpty()) {
        Box(modifier.fillMaxSize().padding(24.dp)) { Text("Nessun proprietario. Premi + per aggiungerne uno.") }
    } else {
        LazyColumn(modifier = modifier) {
            items(owners, key = { it.id }) { o ->
                ListItem(headlineContent = { Text(o.name) }, modifier = Modifier.fillMaxWidth().clickable { onOpenOwner(o.id) }.padding(horizontal = 8.dp, vertical = 4.dp))
                Divider()
            }
        }
    }
}
