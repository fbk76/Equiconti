@file:JvmName("HorseEditScreenFileA") // <— evita il “Duplicate JVM class name”

package com.cz.equiconti.ui.owner.horse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Schermata di edit cavallo (stub che compila).
 * Puoi mantenere la tua UI: il JvmName risolve solo la collisione tra due file omonimi.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseEditScreen(
    horseId: Long?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modifica cavallo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Horse ID: ${horseId ?: 0L}")
            // TODO: qui i campi reali di edit
        }
    }
}
