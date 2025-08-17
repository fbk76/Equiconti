package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons

/**
 * Schermata per aggiungere un cavallo a un proprietario.
 * - Se onSave è NULL, salva automaticamente tramite il suo ViewModel e torna indietro.
 * - In alternativa puoi passare una tua lambda onSave(name, monthlyFeeCents, notes).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseAddScreen(
    ownerId: Long,
    onBack: () -> Unit,
    onSave: ((name: String, monthlyFeeCents: Long, notes: String?) -> Unit)? = null,
    vm: HorseAddViewModel = hiltViewModel()
) {
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var feeText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("0"))
    }
    var notes by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    // converto in Long in sicurezza (vuoto o non numerico => 0)
    val feeCents: Long = feeText.text.toLongOrNull() ?: 0L

    val doSave: () -> Unit = {
        val trimmed = name.text.trim()
        if (trimmed.isNotEmpty()) {
            if (onSave != null) {
                onSave(trimmed, feeCents, notes.text.trim().ifEmpty { null })
            } else {
                vm.addHorse(
                    ownerId = ownerId,
                    name = trimmed,
                    monthlyFeeCents = feeCents,
                    notes = notes.text.trim().ifEmpty { null },
                    onDone = onBack
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo cavallo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = doSave,
                        enabled = name.text.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Salva"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = feeText,
                onValueChange = { feeText = it },
                label = { Text("Quota mensile (cent)") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Note") },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        }
    }
}

/** ViewModel dedicato al salvataggio del cavallo */
@HiltViewModel
class HorseAddViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    fun addHorse(
        ownerId: Long,
        name: String,
        monthlyFeeCents: Long,
        notes: String?,
        onDone: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repo.upsertHorse(
                Horse(
                    id = 0L,
                    ownerId = ownerId,
                    name = name,
                    monthlyFeeCents = monthlyFeeCents,
                    notes = notes
                )
            )
            onDone()
        }
    }
}
