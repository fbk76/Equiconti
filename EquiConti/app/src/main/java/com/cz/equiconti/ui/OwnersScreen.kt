package com.cz.equiconti.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner
import com.cz.equiconti.vm.OwnersVm

/**
 * Lista proprietari: usa Owner.firstName / Owner.lastName.
 */
@Composable
fun OwnersScreen(
    nav: NavController,
    vm: OwnersVm = hiltViewModel()
) {
    val owners by vm.owners.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editing: Owner? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Proprietari") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { editing = null; showDialog = true }) {
                Text("+")
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            LazyColumn {
                items(owners) { o ->
                    ListItem(
                        headlineContent = { Text("${o.firstName} ${o.lastName}".trim()) },
                        supportingContent = { o.phone?.let { Text(it) } },
                        modifier = Modifier.clickable {
                            editing = o
                            showDialog = true
                        }
                    )
                    Divider()
                }
            }
        }
    }

    if (showDialog) {
        OwnerDialog(
            initial = editing,
            onDismiss = { showDialog = false },
            onSave = { saved ->
                // Qui collega la tua logica reale di salvataggio (DAO/Repo) se già presente.
                // Per ora chiudiamo solo il dialog.
                showDialog = false
            }
        )
    }
}
