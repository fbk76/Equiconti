package com.cz.equiconti.ui.txn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTxnScreen(
    ownerId: Long,
    onBack: () -> Unit,
    vm: AddTxnViewModel = hiltViewModel()
) {
    val horses by vm.horses.collectAsState()
    var selectedHorse: Horse? by remember { mutableStateOf(null) }
    var expanded by remember { mutableStateOf(false) }

    var amountTxt by remember { mutableStateOf(TextFieldValue("")) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }

    val canSave = selectedHorse != null && (amountTxt.text.toLongOrNull() ?: 0L) != 0L

    fun doSave() {
        val horse = selectedHorse ?: return
        val cents = amountTxt.text.toLongOrNull() ?: 0L
        if (cents == 0L) return
        vm.saveTxn(
            horseId = horse.id,
            amountCents = cents,
            notes = notes.text.trim().ifEmpty { null },
            onDone = onBack
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo movimento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(onClick = ::doSave, enabled = canSave) {
                        Icon(Icons.Filled.Save, contentDescription = "Salva")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // SELEZIONE CAVALLO
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedHorse?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cavallo") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .clickable { expanded = true }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    horses.forEach { h ->
                        DropdownMenuItem(
                            text = { Text(h.name) },
                            onClick = {
                                selectedHorse = h
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = amountTxt,
                onValueChange = { amountTxt = it },
                label = { Text("Importo in centesimi") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Note (opzionali)") },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            )
        }
    }
}

@HiltViewModel
class AddTxnViewModel @Inject constructor(
    private val repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val ownerId: Long = savedStateHandle.get<Long>("ownerId") ?: 0L

    // cavalli del proprietario per il menu
    val horses: StateFlow<List<Horse>> =
        repo.getHorses(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveTxn(horseId: Long, amountCents: Long, notes: String?, onDone: () -> Unit) {
        viewModelScope.launch {
            repo.upsertTxn(
                Txn(
                    id = 0L,
                    horseId = horseId,
                    amountCents = amountCents,
                    notes = notes
                )
            )
            onDone()
        }
    }
}
