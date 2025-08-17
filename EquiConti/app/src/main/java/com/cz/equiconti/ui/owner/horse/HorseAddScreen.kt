package com.cz.equiconti.ui.owner.horse

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.ui.owner.OwnersViewModel

/**
 * Schermata che usa il form e salva un nuovo cavallo per il dato ownerId,
 * poi torna indietro.
 *
 * N.B. Non richiede più onSave tra i parametri: il salvataggio è gestito qui dentro.
 */
@Composable
fun HorseAddScreen(
    ownerId: Long,
    onBack: () -> Unit,
    vm: OwnersViewModel = hiltViewModel()
) {
    HorseEditFormScreen(
        title = "Nuovo cavallo",
        initialName = "",
        onBack = onBack,
        onSave = { name ->
            vm.upsertHorse(
                Horse(
                    id = 0L,
                    ownerId = ownerId,
                    name = name.trim(),
                    monthlyFeeCents = 0L,  // inizialmente 0; potrai estendere il form
                    notes = null
                )
            )
            onBack()
        }
    )
}
