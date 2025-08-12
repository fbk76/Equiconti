package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cz.equiconti.data.Owner

/**
 * Schermata per inserire un nuovo proprietario.
 * Firma concordata: (nav, onSave)
 */
@Composable
fun AddOwnerScreen(
    nav: NavController,
    onSave: (Owner) -> Unit
) {
    var first by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (first.isNotBlank() || last.isNotBlank()) {
                        onSave(
                            Owner(
                                firstName = first.trim(),
                                lastName  = last.trim(),
                                phone     = phone.trim().ifBlank { null }
                            )
                        )
                        nav.popBackStack()
                    }
                },
                icon = { Icon(Icons.Filled.Check, contentDescription = "Salva") },
                text = { Text("Salva") }
            )
        }
    ) { padding ->
        AddOwnerForm(
            padding = padding,
            first = first,
            onFirst = { first = it },
            last = last,
            onLast = { last = it },
            phone = phone,
            onPhone = { phone = it }
        )
    }
}

@Composable
private fun AddOwnerForm(
    padding: PaddingValues,
    first: String,
    onFirst: (String) -> Unit,
    last: String,
    onLast: (String) -> Unit,
    phone: String,
    onPhone: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = first,
            onValueChange = onFirst,
            label = { Text("Nome") },
            modifier = Modifier.fillMaxSize(fraction = 1f)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = last,
            onValueChange = onLast,
            label = { Text("Cognome") },
            modifier = Modifier.fillMaxSize(fraction = 1f)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = onPhone,
            label = { Text("Telefono (opzionale)") },
            modifier = Modifier.fillMaxSize(fraction = 1f)
        )
    }
}
