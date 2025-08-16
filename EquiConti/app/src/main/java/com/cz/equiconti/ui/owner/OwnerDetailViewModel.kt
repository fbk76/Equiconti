package com.cz.equiconti.ui.owner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OwnerDetailViewModel @Inject constructor(
    private val repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ownerId: Long = checkNotNull(savedStateHandle["ownerId"]) {
        "Missing ownerId in nav arguments"
    }

    /**
     * Flow dell’Owner corrente ricavato dalla lista completa.
     * Non richiede aggiunte ai DAO.
     */
    fun ownerFlow(): Flow<Owner?> =
        repo.getOwners().map { list -> list.firstOrNull { it.id == ownerId } }

    /**
     * Flow dei cavalli dell’owner.
     */
    fun horses(): Flow<List<Horse>> = repo.getHorses(ownerId)

    // Facoltativo: versioni StateFlow pronte per essere collectAsState()
    val ownerState = ownerFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val horsesState = horses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
