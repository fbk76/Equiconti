package com.cz.equiconti.ui.owner.horse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorseViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /** Crea un cavallo legato a ownerId, solo con il nome minimo. */
    fun addHorse(ownerId: Long, name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repo.upsertHorse(
                Horse(
                    id = 0L,
                    ownerId = ownerId,
                    name = name
                )
            )
        }
    }
}
