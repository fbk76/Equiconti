package com.cz.equiconti.ui.owner.horse

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.cz.equiconti.ui.owner.horse.HorseViewModel

@Composable
fun HorseAddScreen(
    ownerId: Long,
    onBack: () -> Unit,
    vm: HorseViewModel = hiltViewModel()
) {
    HorseEditFormScreen(
        title = "Nuovo cavallo",
        initialName = "",
        onBack = onBack,
        onSave = { name ->
            vm.addHorse(ownerId, name)
            onBack()
        }
    )
}
