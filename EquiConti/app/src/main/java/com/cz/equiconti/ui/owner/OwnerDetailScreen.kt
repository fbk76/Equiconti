package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDetailScreen(
    initialName: String = "",
    initialPhone: String = "",
    onSave: (name: String, phone: String) -> Unit,
) {
    val (name, setName) = remember { mutableStateOf(initialName) }
    val (phone, setPhone) = remember { mutableStateOf(initialPhone) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettaglio Proprietario") }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = setName,
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = setPhone,
                label = { Text("Telefono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onSave(name, phone) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Icon(Icons.Default.Save, contentDescription = "Salva")
                    Spacer(Modifier.width(8.dp))
                    Text("Salva")
                }
            }
        }
    }
}
