package com.cz.equiconti.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OwnerDetailViewModel @Inject constructor(
    private val repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ownerId: Long = checkNotNull(savedStateHandle["ownerId"])

    /** Flusso del proprietario corrente (derivato da getOwners()) */
    val owner: StateFlow<Owner?> =
        repo.getOwners()
            .map { list -> list.find { it.id == ownerId } }
            .stateIn(
                scope = this,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    /** Flusso dei cavalli del proprietario */
    val horses: StateFlow<List<Horse>> =
        repo.getHorses(ownerId)
            .stateIn(
                scope = this,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}
