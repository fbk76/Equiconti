package com.cz.equiconti.ui.owner

import androidx.compose.runtime.Composable
import com.cz.equiconti.data.Owner

@Composable
fun AddOwnerScreen(
    onSave: (Owner) -> Unit,
    onBack: () -> Unit
) {
    // UI con i TextField per nome, cognome, telefono...
    // Quando confermi:
    // onSave(Owner(firstName = ..., lastName = ..., phone = ...))
    // Per chiudere senza salvare:
    // onBack()
}
