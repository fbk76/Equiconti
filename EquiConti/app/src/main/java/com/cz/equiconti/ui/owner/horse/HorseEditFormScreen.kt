package com.cz.equiconti.ui.owner.horse

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.ui.owner.OwnersViewModel

/**
 * Schermata che usa il form e salva davvero un nuovo cavallo per un dato ownerId,
 * poi torna indietro.
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
            // monthlyFeeCents a 0: l’utente potrà gestirlo in seguito (o aggiungilo al form se vuoi)
            vm.upsertHorse(
                Horse(
                    id = 0L,
                    ownerId = ownerId,
                    name = name,
                    monthlyFeeCents = 0L,
                    notes = null
                )
            )
            onBack()
        }
    )
}
