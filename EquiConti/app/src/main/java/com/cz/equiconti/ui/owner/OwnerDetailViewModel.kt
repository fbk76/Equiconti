package com.cz.equiconti.ui.owner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OwnerDetailViewModel @Inject constructor(
    private val repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ownerId: Long = checkNotNull(savedStateHandle["ownerId"])

    // Proprietario selezionato
    val owner: StateFlow<Owner?> =
        repo.getOwners()
            .map { list -> list.firstOrNull { it.id == ownerId } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // Cavalli del proprietario
    val horses: StateFlow<List<Horse>> =
        repo.getHorses(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Placeholder: da abilitare quando aggiungerai i metodi di scrittura nel Repo/DAO
    fun addHorse(name: String) {
        // viewModelScope.launch { repo.addHorse(ownerId, name) }
    }
}
