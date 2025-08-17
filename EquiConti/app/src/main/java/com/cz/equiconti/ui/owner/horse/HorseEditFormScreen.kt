package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseEditFormScreen(
    title: String = "Cavallo",
    initialName: String = "",
    onBack: () -> Unit,
    onSave: (String) -> Unit,
    // parametri aggiunti SOLO per compatibilità con chiamate esistenti
    monthlyFeeCents: Long = 0L,
    notes: String? = null,
) {
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(initialName))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
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
                        onClick = { onSave(name.text.trim()) },
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
                label = { Text("Nome cavallo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
