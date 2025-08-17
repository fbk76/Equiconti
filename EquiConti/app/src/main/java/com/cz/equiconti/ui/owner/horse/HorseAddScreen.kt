// app/src/main/java/com/cz/equiconti/ui/owner/horse/HorseAddScreen.kt
package com.cz.equiconti.ui.owner.horse

import androidx.compose.runtime.Composable

@Composable
fun HorseAddScreen(
    ownerId: Long,                 // lo teniamo in firma per coerenza con la rotta
    onBack: () -> Unit,
    onSave: (String) -> Unit       // <-- ora la schermata accetta onSave
) {
    HorseEditFormScreen(
        title = "Nuovo cavallo",
        initialName = "",
        onBack = onBack,
        onSave = { name ->
            onSave(name)            // delega il salvataggio a chi chiama (NavGraph / ViewModel)
        }
    )
}
